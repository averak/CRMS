package dev.abelab.crms.logic;

import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;
import io.jsonwebtoken.*;

import lombok.*;
import dev.abelab.crms.db.entity.User;
import dev.abelab.crms.repository.UserRepository;
import dev.abelab.crms.property.JwtProperty;
import dev.abelab.crms.enums.UserRoleEnum;
import dev.abelab.crms.exception.ErrorCode;
import dev.abelab.crms.exception.BadRequestException;
import dev.abelab.crms.exception.UnauthorizedException;
import dev.abelab.crms.exception.ForbiddenException;

@RequiredArgsConstructor
@Component
public class UserLogic {

    private final UserRepository userRepository;

    private final JwtProperty jwtProperty;

    private final PasswordEncoder passwordEncoder;

    /**
     * 管理者チェック
     *
     * @param userId ユーザID
     */
    public void checkAdmin(final int userId) {
        final var user = this.userRepository.selectById(userId);

        if (user.getRoleId() != UserRoleEnum.ADMIN.getId()) {
            throw new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION);
        }
    }

    /**
     * JWTを発行
     *
     * @param user ユーザ
     *
     * @return JWT
     */
    public String generateJwt(final User user) {
        final var claims = Jwts.claims();
        claims.put(Claims.ISSUER, this.jwtProperty.getIssuer());
        claims.put("id", user.getId());

        return Jwts.builder() //
            .setClaims(claims) //
            .setIssuer(this.jwtProperty.getIssuer()) //
            .setIssuedAt(new Date()) //
            .setExpiration(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)) // 1 week
            .signWith(SignatureAlgorithm.HS512, this.jwtProperty.getSecret().getBytes()) //
            .compact();
    }

    /**
     * ログインユーザを取得
     *
     * @param credentials 資格情報
     *
     * @return ユーザ
     */
    public User getLoginUser(final String credentials) {
        // 資格情報の構文チェック
        if (!credentials.startsWith("Bearer ")) {
            throw new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN);
        }
        final var jwt = credentials.substring(7);

        // JWTの有効性を検証
        try {
            final var claim = Jwts.parser().setSigningKey(this.jwtProperty.getSecret().getBytes()).parseClaimsJws(jwt).getBody();
            final var userId = claim.get("id");

            if (userId == null) {
                throw new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN);
            }

            return this.userRepository.selectById((int) userId);
        } catch (SignatureException e) {
            throw new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN);
        } catch (MalformedJwtException e) {
            throw new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN);
        } catch (UnsupportedJwtException ex) {
            throw new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN);
        } catch (IllegalArgumentException ex) {
            throw new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException(ErrorCode.EXPIRED_ACCESS_TOKEN);
        }
    }

    /**
     * パスワードをハッシュ化
     *
     * @param password パスワード
     *
     * @return ハッシュ値
     */
    public String encodePassword(final String password) {
        return this.passwordEncoder.encode(password);
    }

    /**
     * パスワードが一致するか検証
     *
     * @param user     ユーザ
     *
     * @param password パスワード
     */
    public void verifyPassword(final User user, final String password) {
        if (!this.passwordEncoder.matches(password, user.getPassword())) {
            throw new UnauthorizedException(ErrorCode.WRONG_PASSWORD);
        }
    }

}
