import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import * as moment from 'moment-timezone';

import { ReservationModel } from 'src/app/model/reservation-model';
import { ReservationCreateRequest } from 'src/app/request/reservation-create-request';
import { ReservationService } from 'src/app/shared/services/reservation.service';
import { AlertService } from 'src/app/shared/services/alert.service';

@Component({
  selector: 'app-reservation-new-dialog',
  templateUrl: './reservation-new-dialog.component.html',
  styleUrls: ['./reservation-new-dialog.component.css'],
})
export class ReservationNewDialogComponent implements OnInit {
  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any,
    private reservationService: ReservationService,
    private alertService: AlertService,
    private matDialogRef: MatDialogRef<ReservationNewDialogComponent>
  ) {}

  ngOnInit(): void {}

  handleSubmitReservation(reservation: ReservationModel): void {
    // 予約作成リクエストを作成
    const requestBody: ReservationCreateRequest = {
      startAt: moment(reservation.startAt).tz('Asia/Tokyo').format(),
      finishAt: moment(reservation.finishAt).tz('Asia/Tokyo').format(),
    };

    this.reservationService.createReservation(requestBody).subscribe(
      () => {
        this.reservationService.fetchReservations();
        this.alertService.openSnackBar('予約を追加しました', 'SUCCESS');
        this.matDialogRef.close();
      },
      (error) => {
        this.alertService.openSnackBar(error, 'ERROR');
      }
    );
  }

  handleCancelSubmit(): void {
    this.matDialogRef.close();
  }
}
