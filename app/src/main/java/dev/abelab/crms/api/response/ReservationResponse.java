package dev.abelab.crms.api.response;

import java.util.Date;

import lombok.*;

/**
 * 予約情報レスポンス
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationResponse {

    /**
     * 予約ID
     */
    Integer id;

    /**
     * 予約者
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
