package dev.abelab.crms.service;

import mockit.Injectable;
import mockit.Tested;

import dev.abelab.crms.repository.UserRepository;
import dev.abelab.crms.repository.ReservationRepository;
import dev.abelab.crms.logic.UserLogic;
import dev.abelab.crms.logic.ReservationLogic;

/**
 * ReservationService Unit Test
 */
class ReservationService_UT extends AbstractService_UT {

    @Injectable
    UserLogic userLogic;

    @Injectable
    ReservationLogic reservationLogic;

    @Injectable
    UserRepository userRepository;

    @Injectable
    ReservationRepository reservationRepository;

    @Tested
    ReservationService reservationService;

}
