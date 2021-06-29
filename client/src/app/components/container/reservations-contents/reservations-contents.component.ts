import { Component, OnInit } from '@angular/core';

import { UserModel } from 'src/app/model/user-model';
import { ReservationModel } from 'src/app/model/reservation-model';
import { ReservationsModel } from 'src/app/model/reservations-model';
import { ReservationCreateRequest } from 'src/app/request/reservation-create-request';
import { ReservationService } from 'src/app/shared/services/reservation.service';
import { UserService } from 'src/app/shared/services/user.service';
import { AlertService } from 'src/app/shared/services/alert.service';

@Component({
  selector: 'app-reservations-contents',
  templateUrl: './reservations-contents.component.html',
  styleUrls: ['./reservations-contents.component.css'],
})
export class ReservationsContentsComponent implements OnInit {
  reservations!: ReservationModel[];
  loginUser!: UserModel;

  constructor(
    private reservationService: ReservationService,
    private userService: UserService,
    private alertService: AlertService
  ) {}

  ngOnInit(): void {
    // 予約一覧を取得
    this.reservationService.getReservations().subscribe(
      (reservations: ReservationsModel) => {
        this.reservations = reservations.reservations;
      },
      (error) => {
        this.alertService.openSnackBar(error, 'ERROR');
      }
    );

    // ログインユーザを取得
    this.userService.getLoginUser().subscribe(
      (user: UserModel) => {
        this.loginUser = user;
      },
      (error) => {
        this.alertService.openSnackBar(error, 'ERROR');
      }
    );
  }
}
