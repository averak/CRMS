package dev.abelab.crs.enums;

import java.util.Arrays;

import lombok.*;
import dev.abelab.crs.exception.ErrorCode;
import dev.abelab.crs.exception.NotFoundException;

/**
 * The enum user role
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

    private final int id;

    private final String name;

    /**
     * find by id
     *
     * @param id id
     *
     * @return user role
     */
    public static UserRoleEnum findById(final int id) {
        return Arrays.stream(values()).filter(e -> e.getId() == id) //
            .findFirst().orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_ROLE));
    }

}
