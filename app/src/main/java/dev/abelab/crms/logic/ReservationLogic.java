package dev.abelab.crms.logic;

import java.util.List;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.Comparator;

import org.springframework.stereotype.Component;
import org.modelmapper.ModelMapper;

import lombok.*;
import dev.abelab.crms.db.entity.Reservation;
import dev.abelab.crms.model.ReservationWithUserModel;
import dev.abelab.crms.repository.UserRepository;
import dev.abelab.crms.repository.ReservationRepository;
import dev.abelab.crms.enums.UserRoleEnum;
import dev.abelab.crms.util.DateTimeUtil;
import dev.abelab.crms.exception.ErrorCode;
import dev.abelab.crms.exception.BadRequestException;
import dev.abelab.crms.exception.ConflictException;
import dev.abelab.crms.exception.ForbiddenException;

@RequiredArgsConstructor
@Component
public class ReservationLogic {

    /**
     * 最大予約可能時間
     */
    private final double MAX_RESERVABLE_HOURS = 12.0;

    /**
     * 予約可能開始時刻
     */
    private final double RESERVABLE_START_HOUR = 9.0;

    /**
     * 予約可能終了時刻
     */
    private final double RESERVABLE_FINISH_HOUR = 22.0;

    private final UserRepository userRepository;

    private final ReservationRepository reservationRepository;

    private final ModelMapper modelMapper;

    /**
     * 編集権限があるか確認
     *
     * @param reservationId 予約ID
     *
     * @param userId        ユーザID
     */
    public void checkEditPermission(final int reservationId, final int userId) {
        final var reservation = this.reservationRepository.selectById(reservationId);
        final var user = this.userRepository.selectById(userId);

        // 管理者/予約者のみ編集可能
        if (user.getRoleId() != UserRoleEnum.ADMIN.getId() && !reservation.getUserId().equals(user.getId())) {
            throw new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION);
        }
    }

    /**
     * 削除可能な予約かチェック
     *
     * @param reservation 予約
     */
    public void checkDeletableReservation(final Reservation reservation) {
        // 過去の日時
        final var now = new Date();
        if (now.after(reservation.getStartAt())) {
            throw new BadRequestException(ErrorCode.PAST_RESERVATION_CANNOT_BE_DELETED);
        }
    }

    /**
     * 予約時間のバリデーション
     */
    public void validateReservationTime(final Date startAt, final Date finishAt, final int userId, final int reservationId) {
        // 過去の日時
        final var now = new Date();
        if (now.after(startAt)) {
            throw new BadRequestException(ErrorCode.PAST_RESERVATION_CANNOT_BE_CREATED);
        }

        // 開始時刻よりも前に終了時刻が設定されている
        if (startAt.after(finishAt)) {
            throw new BadRequestException(ErrorCode.INVALID_RESERVATION_TIME);
        }

        // 開始時刻と終了時刻が同じ
        if (startAt.equals(finishAt)) {
            throw new BadRequestException(ErrorCode.INVALID_RESERVATION_TIME);
        }

        // 制限時間を超過している
        if (DateTimeUtil.diffHours(startAt, finishAt) > this.MAX_RESERVABLE_HOURS) {
            throw new BadRequestException(ErrorCode.TOO_LONG_RESERVATION_HOURS);
        }

        // 予約可能範囲（09:00〜22:00）に収まっていない
        if (DateTimeUtil.getHour(startAt) < this.RESERVABLE_START_HOUR || DateTimeUtil.getHour(finishAt) > this.RESERVABLE_FINISH_HOUR) {
            throw new BadRequestException(ErrorCode.NOT_WITHIN_RESERVABLE_TIME_RANGE);
        }

        // 同時刻はすでに予約済み
        final var reservations = this.reservationRepository.selectByUserId(userId);
        reservations.forEach(reservation -> {
            if (reservation.getId() == reservationId) {
                // 過去の予約は更新不可
                if (now.after(reservation.getFinishAt())) {
                    throw new BadRequestException(ErrorCode.PAST_RESERVATION_CANNOT_BE_CHANGED);
                }
                return;
            }

            // 開始時刻が重複
            if (startAt.after(reservation.getStartAt()) && startAt.before(reservation.getFinishAt())) {
                throw new ConflictException(ErrorCode.CONFLICT_RESERVATION_TIME);
            }
            // 終了時刻が重複
            if (finishAt.after(reservation.getStartAt()) && finishAt.before(reservation.getFinishAt())) {
                throw new ConflictException(ErrorCode.CONFLICT_RESERVATION_TIME);
            }
            // 開始時刻，終了時刻共に重複
            if (startAt.after(reservation.getStartAt()) && finishAt.before(reservation.getFinishAt())) {
                throw new ConflictException(ErrorCode.CONFLICT_RESERVATION_TIME);
            }
            // 開始時刻，終了時刻共に完全一致
            if (startAt.equals(reservation.getStartAt()) && finishAt.equals(reservation.getFinishAt())) {
                throw new ConflictException(ErrorCode.CONFLICT_RESERVATION_TIME);
            }
        });
    }

    /**
     * 予約（+ユーザ）を取得
     *
     * @param reservation 予約
     *
     * @return 予約（+ユーザ）
     */
    public ReservationWithUserModel getWithUser(final Reservation reservation) {
        final var user = this.userRepository.selectById(reservation.getUserId());
        final var reservationWithUserModel = this.modelMapper.map(reservation, ReservationWithUserModel.class);
        reservationWithUserModel.setUser(user);
        return reservationWithUserModel;
    }

    /**
     * 翌日の予約一覧を取得
     *
     * @return 予約一覧
     */
    public List<ReservationWithUserModel> getNextDayReservations() {
        // 予約時刻でソート
        final Comparator<ReservationWithUserModel> comparator = Comparator //
            .comparing(ReservationWithUserModel::getStartAt) //
            .thenComparing(ReservationWithUserModel::getFinishAt);

        final var now = new Date();
        return this.reservationRepository.findAll().stream() //
            .map(this::getWithUser) //
            // 翌日の予約のみを抽出
            .filter(reservation -> {
                // 過去の予約
                if (now.after(reservation.getStartAt())) {
                    return false;
                }
                // 翌日以降の予約
                if ((reservation.getStartAt().getTime() - now.getTime()) / (1000 * 60 * 60 * 24) >= 1) {
                    return false;
                }
                return true;
            }) //
            .sorted(comparator) //
            .collect(Collectors.toList());
    }

}
