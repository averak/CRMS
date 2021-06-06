package dev.abelab.crs.annotation;

import java.lang.annotation.*;

import org.junit.jupiter.api.Tag;

/**
 * Integration Test Interface
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Tag("IntegrationTest")
@Inherited
public @interface IntegrationTest {
}
