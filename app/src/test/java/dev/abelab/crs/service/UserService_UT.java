package dev.abelab.crs.service;

import mockit.Injectable;
import mockit.Tested;
import dev.abelab.crs.repository.UserRepository;

/**
 * UserService Unit Test
 */
class UserService_UT extends AbstractService_UT {

    @Injectable
    UserRepository userRepository;

    @Tested
    UserService userService;

}
