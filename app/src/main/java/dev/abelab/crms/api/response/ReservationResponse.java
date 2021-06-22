package dev.abelab.crms.api.response;

import java.util.Date;

import lombok.*;

/**
 * 予約情報レスポンス
 */
@Value
@Builder
@RequiredArgsConstructor
public class ReservationResponse {

    /**
     * 予約ID
     */
    Integer id;

    /**
     * 予約ユーザ
     */
    UserResponse user;

    /**
     * 開始時刻
     */
    Date startAt;

    /**
     * 終了時刻
     */
    Date finishAt;

}
