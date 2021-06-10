package dev.abelab.crms.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis Configuration
 */
@MapperScan("dev.abelab.crms.db.mapper")
@Configuration
public class MyBatisConfig {
}
