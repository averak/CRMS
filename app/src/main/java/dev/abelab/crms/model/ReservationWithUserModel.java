package dev.abelab.crms.model;

import lombok.*;
import dev.abelab.crms.db.entity.User;
import dev.abelab.crms.db.entity.Reservation;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ReservationWithUserModel extends Reservation {

    User user;

}
