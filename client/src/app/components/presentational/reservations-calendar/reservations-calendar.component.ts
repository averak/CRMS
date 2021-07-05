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

  searchName!: string;
  searchAdmissionYear!: number;

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
    // イベントを取得
    this.buildEvents();

    // ユーザ一覧を保管
    let users: UserModel[] = this.reservations.map((reservation: ReservationModel) => {
      return reservation.user;
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

  buildEvents(): void {
    this.events = [];

    const pushEvent = (reservation: ReservationModel) => {
      const editable =
        this.loginUser.id === reservation.user.id || this.userService.checkAdmin(this.loginUser);

      this.events.push({
        reservation: reservation,
        start: new Date(reservation.startAt),
        end: new Date(reservation.finishAt),
        title: this.userService.getUserName(reservation.user),
        color:
          this.loginUser.id === reservation.user.id
            ? ReservationColorEnum.BLUE
            : ReservationColorEnum.YELLOW,
        actions: this.actions,
        resizable: {
          beforeStart: editable,
          afterEnd: editable,
        },
        draggable: editable,
      });
    };

    // イベント一覧
    this.reservations.map((reservation: ReservationModel) => {
      if (this.searchName === undefined && this.searchAdmissionYear === undefined) {
        pushEvent(reservation);
      }
      // ユーザ名&入学年度で絞り込み検索
      else if (this.searchName !== undefined && this.searchAdmissionYear !== undefined) {
        if (
          this.searchName === this.userService.getUserName(reservation.user) &&
          this.searchAdmissionYear === reservation.user.admissionYear
        ) {
          pushEvent(reservation);
        }
      }
      // ユーザ名で絞り込み検索
      else if (this.searchName === this.userService.getUserName(reservation.user)) {
        pushEvent(reservation);
      }
      // 入学年度で絞り込み検索
      else if (this.searchAdmissionYear === reservation.user.admissionYear) {
        pushEvent(reservation);
      }
    });
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
