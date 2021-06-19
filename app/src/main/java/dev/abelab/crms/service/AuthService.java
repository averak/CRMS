package dev.abelab.crms.service;

import static java.lang.String.format;
import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.crypto.password.PasswordEncoder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import lombok.*;
import dev.abelab.crms.repository.UserRepository;
import dev.abelab.crms.api.request.LoginRequest;
import dev.abelab.crms.api.response.LoginResponse;
import dev.abelab.crms.exception.UnauthorizedException;
import dev.abelab.crms.exception.ErrorCode;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final String jwtSecret = "zdtlD3JK56m6wTTgsNFhqzjqP";

    private final String jwtIssuer = "crms.abelab.dev";

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
        final var jwt = Jwts.builder() //
            .setSubject(format("%s,%s", user.getId(), user.getEmail())) //
            .setIssuer(this.jwtIssuer) //
            .setIssuedAt(new Date()) //
            .setExpiration(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)) // 1 week
            .signWith(SignatureAlgorithm.HS512, this.jwtSecret.getBytes()) //
            .compact();

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
