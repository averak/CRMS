package dev.abelab.crms.enums;

import lombok.*;

/**
 * The enum reservation action
 */
@Getter
@AllArgsConstructor
public enum ReservationActionEnum {

    /**
     * 予約追加
     */
    REGISTERED,

    /**
     * 予約変更
     */
    CHANGED,

    /**
     * 予約削除
     */
    DELETED

}
