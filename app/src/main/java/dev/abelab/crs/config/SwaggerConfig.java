package dev.abelab.crs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Setting Swagger-UI
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket dock() {
        return new Docket(DocumentationType.SWAGGER_2) //
            .useDefaultResponseMessages(false) //
            .select() //
            .apis(RequestHandlerSelectors.basePackage("dev.abelab.crs.api.controller")) //
            .build() //
            .apiInfo(apiInfo()) //
            .tags( //
                new Tag("Auth", "認証"), //
                new Tag("User", "ユーザ") //
            );
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder() //
            .title("CRS Internal API") //
            .version("1.0") //
            .build();
    }
}
