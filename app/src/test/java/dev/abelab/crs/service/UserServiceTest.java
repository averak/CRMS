package dev.abelab.crs.service;

import mockit.Injectable;
import mockit.Tested;
import dev.abelab.crs.repository.UserRepository;

/**
 * UserService Test
 */
class UserServiceTest extends AbstractServiceTest {

    @Injectable
    UserRepository userRepository;

    @Tested
    UserService userService;

}
