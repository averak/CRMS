package dev.abelab.crms.service;

import mockit.Injectable;
import mockit.Tested;

import org.springframework.security.crypto.password.PasswordEncoder;

import dev.abelab.crms.logic.UserLogic;
import dev.abelab.crms.logic.UserRoleLogic;
import dev.abelab.crms.repository.UserRepository;

/**
 * UserService Unit Test
 */
class UserService_UT extends AbstractService_UT {

    @Injectable
    UserLogic userLogic;

    @Injectable
    UserRoleLogic userRoleLogic;

    @Injectable
    UserRepository userRepository;

    @Injectable
    PasswordEncoder passwordEncoder;

    @Tested
    UserService userService;

}
