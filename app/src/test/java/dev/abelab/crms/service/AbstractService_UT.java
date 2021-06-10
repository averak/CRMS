package dev.abelab.crms.service;

import org.springframework.stereotype.Service;

import dev.abelab.crms.annotation.UnitTest;

/**
 * Abstract Service Unit Test
 */
@UnitTest
@Service
public abstract class AbstractService_UT {

	static final int SAMPLE_INT = 1;
	static final String SAMPLE_STR = "SAMPLE";

}
