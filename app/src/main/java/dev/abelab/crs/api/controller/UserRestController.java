package dev.abelab.crs.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import dev.abelab.crs.api.request.UserRequest;
import dev.abelab.crs.api.response.UsersResponse;
import dev.abelab.crs.service.UserService;

@Api(tags = "User")
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class UserRestController {

    private final UserService userService;

    /**
     * ユーザ一覧取得API
     *
     * @return ユーザ一覧レスポンス
     */
    @ApiOperation(value = "ユーザ一覧の取得", //
        notes = "ユーザ一覧を取得する。" //
    )
    @ApiResponses( //
        value = { //
                @ApiResponse(code = 200, message = "取得成功", response = UsersResponse.class), //
                @ApiResponse(code = 401, message = "ユーザがログインしていない"), //
                @ApiResponse(code = 403, message = "ユーザに権限がない") //
        })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public UsersResponse getUsers() {
        return this.userService.getUsers();
    }

    /**
     * ユーザ作成API
     *
     * @param userRequest ユーザ作成リクエスト
     */
    @ApiOperation(value = "ユーザの作成", //
        notes = "ユーザを作成する。" //
    )
    @ApiResponses( //
        value = { //
                @ApiResponse(code = 201, message = "作成成功"), //
                @ApiResponse(code = 401, message = "ユーザがログインしていない"), //
                @ApiResponse(code = 403, message = "ユーザに権限がない"), //
                @ApiResponse(code = 409, message = "ユーザが既に存在している"), //
        } //
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser( //
        @Validated @ApiParam(name = "body", required = true, value = "新規ユーザ情報") @RequestBody final UserRequest userRequest //
    ) {
        this.userService.createUser(userRequest);
    }

    /**
     * ユーザ更新API
     *
     * @param userId      ユーザID
     *
     * @param userRequest ユーザ更新リクエスト
     */
    @ApiOperation(value = "ユーザの更新", //
        notes = "ユーザを更新する。" //
    )
    @ApiResponses( //
        value = { //
                @ApiResponse(code = 200, message = "更新成功"), //
                @ApiResponse(code = 401, message = "ユーザがログインしていない"), //
                @ApiResponse(code = 403, message = "ユーザに権限がない"), //
                @ApiResponse(code = 404, message = "ユーザが存在しない"), //
        } //
    )
    @PutMapping(value = "/{user_id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateUser( //
        @ApiParam(name = "user_id", required = true, value = "ユーザID") @PathVariable("user_id") final int userId, //
        @Validated @ApiParam(name = "body", required = true, value = "ユーザ更新情報") @RequestBody final UserRequest userRequest //
    ) {
        // FIXME
    }

    /**
     * ユーザ削除API
     *
     * @param userId ユーザID
     */
    @ApiOperation(value = "ユーザの削除", //
        notes = "ユーザを削除する。" //
    )
    @ApiResponses( //
        value = { //
                @ApiResponse(code = 200, message = "削除成功"), //
                @ApiResponse(code = 401, message = "ユーザがログインしていない"), //
                @ApiResponse(code = 403, message = "ユーザに権限がない"), //
                @ApiResponse(code = 404, message = "ユーザが存在しない"), //
        } //
    )
    @DeleteMapping(value = "/{user_id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser( //
        @ApiParam(name = "user_id", required = true, value = "ユーザID") @PathVariable("user_id") final int userId //
    ) {
        // FIXME
    }

}