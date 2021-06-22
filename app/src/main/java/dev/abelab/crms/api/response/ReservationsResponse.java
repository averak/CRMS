package dev.abelab.crms.api.response;

import java.util.List;

import lombok.*;

/**
 * 予約一覧レスポンス
 */
@Value
@Builder
@RequiredArgsConstructor
public class ReservationsResponse {

    /**
     * 予約リスト
     */
    List<ReservationResponse> reservations;

}
