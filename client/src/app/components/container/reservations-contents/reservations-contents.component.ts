import { Component, OnInit } from '@angular/core';
import * as moment from 'moment-timezone';

import { UserModel } from 'src/app/model/user-model';
import { ReservationModel } from 'src/app/model/reservation-model';
import { ReservationUpdateRequest } from 'src/app/request/reservation-update-request';
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
      (reservations: ReservationModel[]) => {
        this.reservations = reservations;
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

  handleReservationEdit(reservation: ReservationModel): void {
    // 予約更新リクエストを作成
    const requestBody: ReservationUpdateRequest = {
      startAt: moment(reservation.startAt).tz('Asia/Tokyo').format(),
      finishAt: moment(reservation.finishAt).tz('Asia/Tokyo').format(),
    };

    this.alertService.confirmDialog(
      '編集確認',
      '本当に予約を編集しますか？',
      (result: boolean): void => {
        if (result) {
          this.reservationService.updateReservation(reservation.id, requestBody).subscribe(
            () => {
              this.reservationService.fetchReservations();
              this.alertService.openSnackBar('予約を編集しました', 'SUCCESS');
            },
            (error) => {
              this.alertService.openSnackBar(error, 'ERROR');
            }
          );
        }
      }
    );
  }

  handleReservationDelete(reservation: ReservationModel): void {
    this.alertService.confirmDialog(
      '削除確認',
      '本当に予約を削除しますか？',
      (result: boolean): void => {
        if (result) {
          this.reservationService.deleteReservation(reservation.id).subscribe(
            () => {
              this.reservationService.fetchReservations();
              this.alertService.openSnackBar('予約を削除しました', 'SUCCESS');
            },
            (error) => {
              this.alertService.openSnackBar(error, 'ERROR');
            }
          );
        }
      }
    );
  }
}
