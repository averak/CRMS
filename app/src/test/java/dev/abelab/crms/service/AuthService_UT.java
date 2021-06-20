package dev.abelab.crms.service;

import mockit.Injectable;
import mockit.Tested;

import dev.abelab.crms.logic.UserLogic;
import dev.abelab.crms.repository.UserRepository;

/**
 * AuthService Unit Test
 */
class AuthService_UT extends AbstractService_UT {

    @Injectable
    UserLogic userLogic;

    @Injectable
    UserRepository userRepository;

    @Tested
    AuthService authService;

}
