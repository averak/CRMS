package dev.abelab.crs.annotation;

import java.lang.annotation.*;

import org.junit.jupiter.api.Tag;

/**
 * Intagration test interface
 */
@Tag("integrationTest")
@Inherited
public @interface IntegrationTest {
}
