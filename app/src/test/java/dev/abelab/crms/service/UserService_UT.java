package dev.abelab.crms.service;

import mockit.Injectable;
import mockit.Tested;
import dev.abelab.crms.logic.UserLogic;
import dev.abelab.crms.repository.UserRepository;

/**
 * UserService Unit Test
 */
class UserService_UT extends AbstractService_UT {

    @Injectable
    UserLogic userLogic;

    @Injectable
    UserRepository userRepository;

    @Tested
    UserService userService;

}
