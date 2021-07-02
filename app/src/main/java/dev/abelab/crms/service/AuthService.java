package dev.abelab.crms.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.*;
import dev.abelab.crms.repository.UserRepository;
import dev.abelab.crms.api.request.LoginRequest;
import dev.abelab.crms.logic.UserLogic;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserLogic userLogic;

    private final UserRepository userRepository;

    /**
     * ログイン処理
     *
     * @param requestBody ログインリクエスト
     *
     * @return JWT
     */
    @Transactional
    public String login(final LoginRequest requestBody) {
        // ユーザ情報を取得
        final var user = this.userRepository.selectByEmail(requestBody.getEmail());

        // パスワードチェック
        this.userLogic.verifyPassword(user.getId(), requestBody.getPassword());

        // JWTを発行
        return this.userLogic.generateJwt(user);
    }

}
