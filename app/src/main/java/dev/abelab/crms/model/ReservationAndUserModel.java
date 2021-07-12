package dev.abelab.crms.model;

import lombok.*;
import dev.abelab.crms.db.entity.User;
import dev.abelab.crms.db.entity.Reservation;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationAndUserModel {

    Reservation reservation;

    User user;

}
