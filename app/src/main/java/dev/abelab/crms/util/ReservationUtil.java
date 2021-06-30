package dev.abelab.crms.util;

import dev.abelab.crms.db.entity.Reservation;
import dev.abelab.crms.db.entity.User;
import dev.abelab.crms.api.response.ReservationResponse;

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

}
