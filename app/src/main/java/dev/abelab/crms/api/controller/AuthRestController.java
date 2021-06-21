package dev.abelab.crms.api.controller;

import javax.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import dev.abelab.crms.api.request.LoginRequest;
import dev.abelab.crms.service.AuthService;

@Api(tags = "Auth")
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class AuthRestController {

    private final AuthService authService;

    /**
     * ログイン処理API
     *
     * @param requestBody ログイン情報
     *
     * @return JWT
     */
    @ApiOperation(value = "ログイン", //
        notes = "ユーザのログイン処理を行う。" //
    )
    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "ログイン成功"), //
            @ApiResponse(code = 401, message = "パスワードが間違っている"), //
            @ApiResponse(code = 404, message = "ユーザが存在しない"), //
    })
    @PostMapping(value = "/login")
    public void login( //
        @Validated @ApiParam(name = "body", required = true, value = "ログイン情報") @RequestBody final LoginRequest requestBody, //
        final HttpServletResponse response //
    ) {
        this.authService.login(requestBody, response);
    }

}
