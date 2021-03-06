package dev.abelab.crms.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.*;

@Data
@Configuration
@ConfigurationProperties("jwt")
public class JwtProperty {

    /**
     * Secret key
     */
    String secret;

    /**
     * Issuer
     */
    String issuer;

}
