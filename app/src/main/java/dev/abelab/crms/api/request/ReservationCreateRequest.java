package dev.abelab.crms.api.request;

import java.util.Date;

import lombok.*;

/**
 * 予約作成リクエスト
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationCreateRequest {

    /**
     * 開始時刻
     */
    Date startAt;

    /**
     * 終了時刻
     */
    Date finishAt;

}
