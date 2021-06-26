package dev.abelab.crms.api.request;

import java.util.Date;

import javax.validation.constraints.NotNull;

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
    @NotNull
    Date startAt;

    /**
     * 終了時刻
     */
    @NotNull
    Date finishAt;

}
