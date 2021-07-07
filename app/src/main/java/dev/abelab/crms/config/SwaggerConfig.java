package dev.abelab.crms.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import dev.abelab.crms.property.CrmsProperty;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Autowired
    CrmsProperty crmsProperty;

    @Bean
    public Docket dock() {
        return new Docket(DocumentationType.SWAGGER_2) //
            .useDefaultResponseMessages(false) //
            .protocols(Collections.singleton(this.crmsProperty.getProtocol())) //
            .host(this.crmsProperty.getHostname()) //
            .select() //
            .apis(RequestHandlerSelectors.basePackage("dev.abelab.crms.api.controller")) //
            .build() //
            .apiInfo(apiInfo()) //
            .tags( //
                new Tag("Auth", "認証"), //
                new Tag("Batch", "バッチ処理"), //
                new Tag("Reservation", "予約"), //
                new Tag("User", "ユーザ") //
            );
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder() //
            .title("CRMS Internal API") //
            .version("1.0") //
            .build();
    }
}
