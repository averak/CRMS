package dev.abelab.crms.service;

import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.*;
import dev.abelab.crms.repository.UserRepository;
import dev.abelab.crms.api.request.LoginRequest;
import dev.abelab.crms.logic.UserLogic;
import dev.abelab.crms.util.AuthUtil;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserLogic userLogic;

    private final UserRepository userRepository;

    /**
     * ログイン処理
     *
     * @param requestBody ログインリクエスト
     */
    @Transactional
    public void login(final LoginRequest requestBody, final HttpServletResponse response) {
        // ユーザ情報を取得
        final var user = this.userRepository.selectByEmail(requestBody.getEmail());

        // パスワードチェック
        this.userLogic.verifyPassword(user.getId(), requestBody.getPassword());

        // JWTを発行
        final var jwt = AuthUtil.generateJwt(user);
        response.setHeader("AUTHORIZATION", jwt);
    }

}
