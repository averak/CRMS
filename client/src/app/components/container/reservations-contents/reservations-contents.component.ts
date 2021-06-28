import { Component, OnInit } from '@angular/core';

import { ReservationModel } from 'src/app/model/reservation-model';
import { ReservationsModel } from 'src/app/model/reservations-model';
import { ReservationCreateRequest } from 'src/app/request/reservation-create-request';
import { ReservationService } from 'src/app/shared/services/reservation.service';
import { AlertService } from 'src/app/shared/services/alert.service';

@Component({
  selector: 'app-reservations-contents',
  templateUrl: './reservations-contents.component.html',
  styleUrls: ['./reservations-contents.component.css'],
})
export class ReservationsContentsComponent implements OnInit {
  constructor(private reservationService: ReservationService, private alertService: AlertService) {}

  ngOnInit(): void {}

  handleSubmitReservation(reservation: ReservationModel): void {
    // 予約作成リクエストを作成
    const requestBody: ReservationCreateRequest = {
      startAt: reservation.startAt,
      finishAt: reservation.finishAt,
    };

    this.reservationService.createReservation(requestBody).subscribe(
      () => {
        this.alertService.openSnackBar('予約を追加しました', 'SUCCESS');
      },
      (error) => {
        this.alertService.openSnackBar(error, 'ERROR');
      }
    );

    this.reservationService.getReservations().subscribe(
      (data) => {
        console.log(data);
      },
      (error) => {
        this.alertService.openSnackBar(error, 'ERROR');
      }
    );
  }
}
