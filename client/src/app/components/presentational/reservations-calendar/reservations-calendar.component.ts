import {
  Component,
  OnInit,
  Input,
  Output,
  OnChanges,
  SimpleChanges,
  EventEmitter,
  ChangeDetectionStrategy,
  ViewChild,
  TemplateRef,
} from '@angular/core';
import { Subject } from 'rxjs';
import {
  CalendarEventAction,
  CalendarView,
  CalendarEventTimesChangedEvent,
} from 'angular-calendar';
import { MatDialog } from '@angular/material/dialog';

import { UserModel } from 'src/app/model/user-model';
import { ReservationModel, CalendarEventWithReservation } from 'src/app/model/reservation-model';
import { ReservationColorEnum } from 'src/app/enums/reservation-color-enum';
import { ReservationNewDialogComponent } from 'src/app/components/container/reservation-new-dialog/reservation-new-dialog.component';
import { UserService } from 'src/app/shared/services/user.service';
import { AdmissionYearService } from 'src/app/shared/services/admission-year.service';

@Component({
  selector: 'app-reservations-calendar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './reservations-calendar.component.html',
  styleUrls: ['./reservations-calendar.component.css'],
})
export class ReservationsCalendarComponent implements OnInit, OnChanges {
  @Input() reservations!: ReservationModel[];
  @Input() loginUser!: UserModel;

  @Output() reservationEdit: EventEmitter<any> = new EventEmitter<any>();
  @Output() reservationDelete: EventEmitter<any> = new EventEmitter<any>();

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
    private userService: UserService,
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

  eventTimesChanged(changedEvent: CalendarEventTimesChangedEvent): void {
    this.events.map((event) => {
      if (event === changedEvent.event) {
        const reservation = event.reservation;
        reservation.startAt = changedEvent.newStart as Date;
        reservation.finishAt = changedEvent.newEnd as Date;
        this.reservationEdit.emit(reservation);
      }
    });
  }

  deleteEvent(eventToDelete: CalendarEventWithReservation) {
    this.reservationDelete.emit(eventToDelete.reservation);
  }

  onClickCreateButton(): void {
    this.matDialog.open(ReservationNewDialogComponent, {
      disableClose: true,
    });
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes.reservations !== undefined) {
      this.reservations = changes.reservations.currentValue;
      this.ngOnInit();
    }
  }
}
