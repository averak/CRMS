package dev.abelab.crms.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import io.swagger.annotations.*;
import lombok.*;
import dev.abelab.crms.api.request.UserCreateRequest;
import dev.abelab.crms.api.request.UserUpdateRequest;
import dev.abelab.crms.api.request.LoginUserUpdateRequest;
import dev.abelab.crms.api.request.LoginUserPasswordUpdateRequest;
import dev.abelab.crms.api.response.UsersResponse;
import dev.abelab.crms.api.response.UserResponse;
import dev.abelab.crms.service.UserService;

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
     * @param jwt JWT
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
    public UsersResponse getUsers( //
        @RequestHeader(name = "Authorization", required = true) final String jwt //
    ) {
        return this.userService.getUsers(jwt);
    }

    /**
     * ユーザ作成API
     *
     * @param jwt         JWT
     *
     * @param requestBody ユーザ作成リクエスト
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
        @RequestHeader(name = "Authorization", required = true) final String jwt, //
        @Validated @ApiParam(name = "body", required = true, value = "新規ユーザ情報") @RequestBody final UserCreateRequest requestBody //
    ) {
        this.userService.createUser(requestBody, jwt);
    }

    /**
     * ユーザ更新API
     *
     * @param jwt         JWT
     *
     * @param userId      ユーザID
     *
     * @param requestBody ユーザ更新リクエスト
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
        @RequestHeader(name = "Authorization", required = true) final String jwt, //
        @ApiParam(name = "user_id", required = true, value = "ユーザID") @PathVariable("user_id") final int userId, //
        @Validated @ApiParam(name = "body", required = true, value = "ユーザ更新情報") @RequestBody final UserUpdateRequest requestBody //
    ) {
        this.userService.updateUser(userId, requestBody, jwt);
    }

    /**
     * ユーザ削除API
     *
     * @param jwt    JWT
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
        @RequestHeader(name = "Authorization", required = true) final String jwt, //
        @ApiParam(name = "user_id", required = true, value = "ユーザID") @PathVariable("user_id") final int userId //
    ) {
        this.userService.deleteUser(userId, jwt);
    }

    /**
     * ログインユーザ詳細取得API
     *
     * @param jwt JWT
     *
     * @return ユーザ詳細レスポンス
     */
    @ApiOperation(value = "ログインユーザ詳細の取得", //
        notes = "ログインユーザ詳細を取得する。" //
    )
    @ApiResponses( //
        value = { //
                @ApiResponse(code = 200, message = "取得成功", response = UserResponse.class), //
                @ApiResponse(code = 401, message = "ユーザがログインしていない"), //
                @ApiResponse(code = 404, message = "ユーザが存在しない") //
        })
    @GetMapping(value = "/me")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse getLoginUser( //
        @RequestHeader(name = "Authorization", required = true) final String jwt //
    ) {
        return this.userService.getLoginUser(jwt);
    }

    /**
     * ログインユーザ更新API
     *
     * @param jwt         JWT
     *
     * @param requestBody ログインユーザ更新リクエスト
     */
    @ApiOperation(value = "ログインユーザの更新", //
        notes = "ログインユーザを更新する。" //
    )
    @ApiResponses( //
        value = { //
                @ApiResponse(code = 200, message = "更新成功"), //
                @ApiResponse(code = 401, message = "ユーザがログインしていない"), //
        } //
    )
    @PutMapping(value = "/me")
    @ResponseStatus(HttpStatus.OK)
    public void updateLoginUser( //
        @RequestHeader(name = "Authorization", required = true) final String jwt, //
        @Validated @ApiParam(name = "body", required = true, value = "ユーザ更新情報") @RequestBody final LoginUserUpdateRequest requestBody //
    ) {
        this.userService.updateLoginUser(requestBody, jwt);
    }

    /**
     * ログインユーザのパスワード更新API
     *
     * @param jwt         JWT
     *
     * @param requestBody ログインユーザのパスワード更新リクエスト
     */
    @ApiOperation(value = "ログインユーザのパスワード更新", //
        notes = "ログインユーザのパスワードを更新する。" //
    )
    @ApiResponses( //
        value = { //
                @ApiResponse(code = 200, message = "更新成功"), //
                @ApiResponse(code = 401, message = "ユーザがログインしていない"), //
        } //
    )
    @PutMapping(value = "/me/password")
    @ResponseStatus(HttpStatus.OK)
    public void updateLoginUserPassword( //
        @RequestHeader(name = "Authorization", required = true) final String jwt, //
        @Validated @ApiParam(name = "body", required = true, value = "パスワード更新情報")
        @RequestBody final LoginUserPasswordUpdateRequest requestBody //
    ) {
        this.userService.updateLoginPasswordUser(requestBody, jwt);
    }

}
