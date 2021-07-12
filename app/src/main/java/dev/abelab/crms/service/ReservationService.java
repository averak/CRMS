package dev.abelab.crms.service;

import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.*;
import dev.abelab.crms.db.entity.Reservation;
import dev.abelab.crms.model.ReservationAndUserModel;
import dev.abelab.crms.repository.UserRepository;
import dev.abelab.crms.repository.ReservationRepository;
import dev.abelab.crms.api.request.ReservationCreateRequest;
import dev.abelab.crms.api.request.ReservationUpdateRequest;
import dev.abelab.crms.api.response.ReservationsResponse;
import dev.abelab.crms.logic.UserLogic;
import dev.abelab.crms.logic.ReservationLogic;
import dev.abelab.crms.client.SlackClient;
import dev.abelab.crms.util.ReservationUtil;

@RequiredArgsConstructor
@Service
public class ReservationService {

    private final UserLogic userLogic;

    private final ReservationLogic reservationLogic;

    private final UserRepository userRepository;

    private final ReservationRepository reservationRepository;

    private final SlackClient slackClient;

    /**
     * 予約一覧を取得
     *
     * @param jwt JWT
     *
     * @return 予約一覧レスポンス
     */
    @Transactional
    public ReservationsResponse getReservations(final String jwt) {
        // ログインユーザを取得
        this.userLogic.getLoginUser(jwt);

        final var reservations = this.reservationRepository.findAll();
        final var reservationResponses = reservations.stream().map(reservation -> {
            // 予約者を取得
            final var user = this.userRepository.selectById(reservation.getUserId());
            return ReservationUtil.buildReservationResponse(reservation, user);
        }).collect(Collectors.toList());

        return new ReservationsResponse(reservationResponses);
    }

    /**
     * 予約を作成
     *
     * @param jwt         JWT
     *
     * @param requestBody 予約作成リクエスト
     */
    @Transactional
    public void createReservation(final String jwt, final ReservationCreateRequest requestBody) {
        // ログインユーザを取得
        final var loginUser = this.userLogic.getLoginUser(jwt);

        // 開始時刻と終了時刻のバリデーション
        this.reservationLogic.validateReservationTime(requestBody.getStartAt(), requestBody.getFinishAt(), loginUser.getId(), 0);

        // 予約の作成
        final var reservation = Reservation.builder() //
            .userId(loginUser.getId()) //
            .startAt(requestBody.getStartAt()) //
            .finishAt(requestBody.getFinishAt()) //
            .build();
        this.reservationRepository.insert(reservation);
    }

    /**
     * 予約を更新
     *
     * @param jwt         JWT
     *
     * @param requestBody 予約更新リクエスト
     */
    @Transactional
    public void updateReservation(final String jwt, final int reservationId, final ReservationUpdateRequest requestBody) {
        // ログインユーザを取得
        final var loginUser = this.userLogic.getLoginUser(jwt);

        // 権限をチェック
        this.reservationLogic.checkEditPermission(reservationId, loginUser.getId());

        // 開始時刻と終了時刻のバリデーション
        final var userId = this.reservationRepository.selectById(reservationId).getUserId();
        this.reservationLogic.validateReservationTime(requestBody.getStartAt(), requestBody.getFinishAt(), userId, reservationId);

        final var reservation = this.reservationRepository.selectById(reservationId);
        reservation.setStartAt(requestBody.getStartAt());
        reservation.setFinishAt(requestBody.getFinishAt());
        this.reservationRepository.update(reservation);
    }

    /**
     * 予約を削除
     *
     * @param jwt           JWT
     *
     * @param reservationId 予約ID
     */
    @Transactional
    public void deleteReservation(final String jwt, final int reservationId) {
        // ログインユーザを取得
        final var loginUser = this.userLogic.getLoginUser(jwt);

        // 権限をチェック
        this.reservationLogic.checkEditPermission(reservationId, loginUser.getId());

        this.reservationRepository.deleteById(reservationId);
    }

    /**
     * 予約の抽選
     */
    @Transactional
    public void lotteryReservations() {
        // 翌日の予約一覧を取得
        final var now = new Date();
        final var reservations = this.reservationRepository.findAll().stream() //
            .filter(reservation -> {
                // 過去の予約
                if (now.before(reservation.getStartAt())) {
                    return false;
                }
                // 翌日以降の予約
                if ((reservation.getStartAt().getTime() - now.getTime()) / 1000 * 60 * 60 * 24 > 1.0) {
                    return false;
                }
                return true;
            }).map(reservation -> {
                final var user = this.userRepository.selectById(reservation.getUserId());
                return ReservationAndUserModel.builder() //
                    .reservation(reservation) //
                    .user(user) //
                    .build();
            }).collect(Collectors.toList());

        // 抽選結果をSlackに送信
        if (!reservations.isEmpty()) {
            this.slackClient.sendLotteryResult(reservations);
        }
    }

}
