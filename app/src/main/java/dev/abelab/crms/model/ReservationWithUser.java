package dev.abelab.crms.model;

import lombok.*;
import dev.abelab.crms.db.entity.User;
import dev.abelab.crms.db.entity.Reservation;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ReservationWithUser extends Reservation {

    User user;

}
