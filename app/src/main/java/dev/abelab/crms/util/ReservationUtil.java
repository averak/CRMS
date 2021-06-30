package dev.abelab.crms.util;

import java.util.Date;

import dev.abelab.crms.db.entity.Reservation;
import dev.abelab.crms.db.entity.User;
import dev.abelab.crms.api.response.ReservationResponse;
import dev.abelab.crms.exception.ErrorCode;
import dev.abelab.crms.exception.BadRequestException;

public class ReservationUtil {

    /**
     * 予約レスポンスを作成
     *
     * @param reservation 予約
     *
     * @param user        予約者
     *
     * @return 予約レスポンス
     */
    public static ReservationResponse buildReservationResponse(final Reservation reservation, final User user) {
        final var userResponse = UserUtil.buildUserResponse(user);
        return ReservationResponse.builder() //
            .id(reservation.getId()) //
            .user(userResponse) //
            .startAt(reservation.getStartAt()) //
            .finishAt(reservation.getFinishAt()) //
            .build();
    }

    /**
     * 予約時間のバリデーション
     */
    public static void validateReservationTime(final Date startAt, final Date finishAt) {
        // 過去の日時
        final var now = new Date();
        if (now.after(startAt)) {
            throw new BadRequestException(ErrorCode.INVALID_RESERVATION);
        }

        // 開始時刻よりも前に終了時刻が設定されている
        if (startAt.after(finishAt)) {
            throw new BadRequestException(ErrorCode.INVALID_RESERVATION);
        }

        // 開始時刻と終了時刻が同じ
        if (startAt.equals(finishAt)) {
            throw new BadRequestException(ErrorCode.INVALID_RESERVATION);
        }
    }

}
