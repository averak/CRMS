package dev.abelab.crms.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.*;

@Data
@Configuration
@ConfigurationProperties("crms")
public class CrmsProperty {

    /**
     * protocol
     */
    String protocol;

    /**
     * host name
     */
    String hostname;

}
