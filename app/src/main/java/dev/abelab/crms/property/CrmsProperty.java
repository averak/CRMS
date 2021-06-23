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

    /**
     * Admin
     */
    Admin admin;

    @Data
    public static class Admin {

        /**
         * Admin first name
         */
        String firstName;

        /**
         * Admin last name
         */
        String lastName;

        /**
         * Admin email
         */
        String email;

        /**
         * Admin password
         */
        String password;

        /**
         * Admin admission year
         */
        Integer admissionYear;

    }

}
