package dev.abelab.crs.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis Configuration
 */
@MapperScan("dev.abelab.crs.db.mapper")
@Configuration
public class MyBatisConfig {
}
