import { Component, OnInit } from '@angular/core';

import { ReservationsModel } from 'src/app/model/reservations-model';
import { ReservationCreateRequest } from 'src/app/request/reservation-create-request';
import { ReservationService } from 'src/app/shared/services/reservation.service';

@Component({
  selector: 'app-reservations-contents',
  templateUrl: './reservations-contents.component.html',
  styleUrls: ['./reservations-contents.component.css'],
})
export class ReservationsContentsComponent implements OnInit {
  constructor(private reservationService: ReservationService) {}

  ngOnInit(): void {}

  onCreateClick(): void {
    const requestBody: ReservationCreateRequest = {
      startAt: new Date('2020/06/28 14:03:22'),
      finishAt: new Date(),
    };
    this.reservationService.createReservation(requestBody).subscribe((error) => {
      console.log(error);
    });

    this.reservationService.getReservations().subscribe(
      (reservations: ReservationsModel) => {
        console.log(reservations);
      },
      (error) => {
        console.log(error);
      }
    );
  }
}
