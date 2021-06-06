package dev.abelab.crs.annotation;

import java.lang.annotation.*;

import org.junit.jupiter.api.Tag;

/**
 * Integration Test Interface
 */
@Tag("integrationTest")
@Inherited
public @interface IntegrationTest {
}
