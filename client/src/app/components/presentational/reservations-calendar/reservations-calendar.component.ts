import {
  Component,
  OnInit,
  Input,
  ChangeDetectionStrategy,
  ViewChild,
  TemplateRef,
} from '@angular/core';
import { Subject } from 'rxjs';
import { CalendarEventAction, CalendarView } from 'angular-calendar';
import { MatDialog } from '@angular/material/dialog';

import { UserModel } from 'src/app/model/user-model';
import { CalendarEventWithReservation } from 'src/app/model/calendar-event-with-reservation';
import { ReservationModel } from 'src/app/model/reservation-model';
import { ReservationColorEnum } from 'src/app/enums/reservation-color-enum';
import { ReservationNewDialogComponent } from 'src/app/components/container/reservation-new-dialog/reservation-new-dialog.component';
import { ReservationService } from 'src/app/shared/services/reservation.service';
import { UserService } from 'src/app/shared/services/user.service';
import { AlertService } from 'src/app/shared/services/alert.service';
import { AdmissionYearService } from 'src/app/shared/services/admission-year.service';

@Component({
  selector: 'app-reservations-calendar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './reservations-calendar.component.html',
  styleUrls: ['./reservations-calendar.component.css'],
})
export class ReservationsCalendarComponent implements OnInit {
  @Input() reservations!: ReservationModel[];
  @Input() loginUser!: UserModel;

  @ViewChild('modalContent', { static: true }) modalContent!: TemplateRef<any>;

  view: CalendarView = CalendarView.Week;
  viewDate: Date = new Date();
  events!: CalendarEventWithReservation[];
  userNames: string[] = [];
  admissionYears!: number[];

  actions: CalendarEventAction[] = [
    {
      label: '<i class="fas fa-fw fa-trash-alt"></i>',
      a11yLabel: 'Delete',
      onClick: ({ event }: { event: any }): void => {
        this.deleteEvent(event);
      },
    },
  ];

  constructor(
    private matDialog: MatDialog,
    private reservationService: ReservationService,
    private userService: UserService,
    private alertService: AlertService,
    private admissionYearService: AdmissionYearService
  ) {}

  ngOnInit(): void {
    let users: UserModel[] = [];
    // イベント一覧
    this.events = this.reservations.map((reservation: ReservationModel) => {
      // ユーザ一覧を保管
      users.push(reservation.user);

      return {
        reservation: reservation,
        start: new Date(reservation.startAt),
        end: new Date(reservation.finishAt),
        title: this.userService.getUserName(reservation.user),
        color:
          this.loginUser.id === reservation.user.id
            ? ReservationColorEnum.BLUE
            : ReservationColorEnum.YELLOW,
        actions: this.actions,
      };
    });

    // 予約者のユーザ名リストを取得
    users = this.userService.sortUsers(users);
    this.userNames = users.map((user) => {
      return this.userService.getUserName(user);
    });
    this.userNames = [...new Set(this.userNames)];

    // 入学年度一覧
    this.admissionYears = this.admissionYearService.getAdmissionYears();
  }

  refresh: Subject<any> = new Subject();
  activeDayIsOpen: boolean = true;

  deleteEvent(eventToDelete: CalendarEventWithReservation) {
    this.alertService.confirmDialog(
      '削除確認',
      '本当に予約を削除しますか？',
      (result: boolean): void => {
        if (result) {
          // FIXME
          this.reservationService.deleteReservation(eventToDelete.reservation.id).subscribe(
            () => {
              this.events = this.events.filter((event) => event !== eventToDelete);
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

  setView(view: CalendarView) {
    this.view = view;
  }

  closeOpenMonthViewDay() {
    this.activeDayIsOpen = false;
  }

  onClickCreateButton(): void {
    this.matDialog.open(ReservationNewDialogComponent, {
      disableClose: true,
    });
  }
}
