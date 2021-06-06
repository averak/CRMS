package dev.abelab.crs.repository;

import org.springframework.stereotype.Repository;

import dev.abelab.crs.annotation.UnitTest;

/**
 * Abstract Repository Unit Test
 */
@UnitTest
@Repository
public abstract class AbstractRepository_UT {

	static final int SAMPLE_INT = 1;
	static final String SAMPLE_STR = "SAMPLE";

}
