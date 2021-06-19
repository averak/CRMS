package dev.abelab.crms.service;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.*;
import dev.abelab.crms.repository.UserRepository;
import dev.abelab.crms.api.request.LoginRequest;
import dev.abelab.crms.exception.UnauthorizedException;
import dev.abelab.crms.exception.ErrorCode;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;

    private final HttpSession httpSession;

    /**
     * ログイン処理
     *
     * @param requestBody ログインリクエスト
     */
    @Transactional
    public void login(final LoginRequest requestBody) {
        // ユーザ情報を取得
        final var user = this.userRepository.selectByEmail(requestBody.getEmail());

        // パスワードチェック
        if (user.getPassword() != requestBody.getPassword()) {
            throw new UnauthorizedException(ErrorCode.WRONG_PASSWORD);
        }

        // FIXME: セッション管理
    }

    /**
     * ログアウト処理
     */
    @Transactional
    public void logout() {
        // FIXME: セッション管理
    }

}
