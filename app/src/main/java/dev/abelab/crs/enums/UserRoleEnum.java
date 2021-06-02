package dev.abelab.crs.enums;

import java.util.Arrays;
import lombok.*;

/**
 * ユーザロール
 */
@Getter
@AllArgsConstructor
public enum UserRoleEnum {

    /**
     * 管理者
     */
    ADMIN(1, "管理者"),

    /**
     * 利用者
     */
    MEMBER(2, "利用者");

    Integer id;

    String name;

}
