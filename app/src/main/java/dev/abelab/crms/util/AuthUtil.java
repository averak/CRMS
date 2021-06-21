package dev.abelab.crms.util;

import java.util.Date;

import io.jsonwebtoken.*;

import dev.abelab.crms.db.entity.User;
import dev.abelab.crms.exception.ErrorCode;
import dev.abelab.crms.exception.UnauthorizedException;

public class AuthUtil {

    private static final String JWT_SECRET = "zdtlD3JK56m6wTTgsNFhqzjqP";

    private static final String JWT_ISSUER = "crms.abelab.dev";

    /**
     * JWTを発行
     *
     * @param user ユーザ
     *
     * @return JWT
     */
    public static String generateJwt(final User user) {
        final var claims = Jwts.claims();
        claims.put(Claims.ISSUER, JWT_ISSUER);
        claims.put("id", user.getId());

        return Jwts.builder() //
            .setClaims(claims) //
            .setIssuer(JWT_ISSUER) //
            .setIssuedAt(new Date()) //
            .setExpiration(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)) // 1 week
            .signWith(SignatureAlgorithm.HS512, JWT_SECRET.getBytes()) //
            .compact();
    }

    /**
     * JWTの有効性を検証
     *
     * @param jwt JWT
     */
    public static void verifyJwt(final String jwt) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET.getBytes()).parseClaimsJws(jwt).getBody();
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
     * JWTの所持ユーザIDを取得
     *
     * @param jwt JWT
     *
     * @return JWTを所持しているユーザID
     */
    public static int getUserIdFromJwt(final String jwt) {
        final var claim = Jwts.parser().setSigningKey(JWT_SECRET.getBytes()).parseClaimsJws(jwt).getBody();
        final var userId = claim.get("id");

        // 無効なJWT
        if (userId == null) {
            throw new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN);
        }

        return (int) userId;
    }

}
