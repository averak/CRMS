package dev.abelab.crms.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.*;
import dev.abelab.crms.repository.UserRepository;
import dev.abelab.crms.api.request.LoginRequest;
import dev.abelab.crms.api.response.LoginResponse;
import dev.abelab.crms.util.AuthUtil;
import dev.abelab.crms.exception.UnauthorizedException;
import dev.abelab.crms.exception.ErrorCode;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    /**
     * ログイン処理
     *
     * @param requestBody ログインリクエスト
     */
    @Transactional
    public LoginResponse login(final LoginRequest requestBody) {
        // ユーザ情報を取得
        final var user = this.userRepository.selectByEmail(requestBody.getEmail());

        // パスワードチェック
        if (!this.passwordEncoder.matches(requestBody.getPassword(), user.getPassword())) {
            throw new UnauthorizedException(ErrorCode.WRONG_PASSWORD);
        }

        // JWTを発行
        final var jwt = AuthUtil.generateJwt(user);

        return LoginResponse.builder().jwt(jwt).build();
    }

    /**
     * ログアウト処理
     */
    @Transactional
    public void logout() {
        // FIXME: セッション管理
    }

}
