import {
  Component,
  OnInit,
  Input,
  Output,
  EventEmitter,
  ChangeDetectionStrategy,
  ViewChild,
  TemplateRef,
} from '@angular/core';
import { Subject } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import {
  CalendarEvent,
  CalendarEventAction,
  CalendarEventTimesChangedEvent,
  CalendarView,
} from 'angular-calendar';
import { MatDialog } from '@angular/material/dialog';

import { UserModel } from 'src/app/model/user-model';
import { ReservationModel } from 'src/app/model/reservation-model';
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
export class ReservationsCalendarComponent implements OnInit {
  @Input() reservations!: ReservationModel[];
  @Input() loginUser!: UserModel;
  @Output() submitReservation: EventEmitter<any> = new EventEmitter<any>();

  @ViewChild('modalContent', { static: true }) modalContent!: TemplateRef<any>;

  view: CalendarView = CalendarView.Week;
  viewDate: Date = new Date();
  events!: CalendarEvent[];
  userNames: string[] = [];
  admissionYears!: number[];

  modalData!: {
    action: string;
    event: CalendarEvent;
  };

  constructor(
    private modal: NgbModal,
    private matDialog: MatDialog,
    private userService: UserService,
    private admissionYearService: AdmissionYearService
  ) {}

  ngOnInit(): void {
    let users: UserModel[] = [];
    // イベント一覧
    this.events = this.reservations.map((reservation: ReservationModel) => {
      // ユーザ一覧を保管
      users.push(reservation.user);

      const isOwnReservation: boolean = this.loginUser.id === reservation.user.id;
      return {
        start: new Date(reservation.startAt),
        end: new Date(reservation.finishAt),
        title: this.userService.getUserName(reservation.user),
        color: isOwnReservation ? ReservationColorEnum.BLUE : ReservationColorEnum.YELLOW,
        resizable: {
          beforeStart: isOwnReservation,
          afterEnd: isOwnReservation,
        },
        draggable: isOwnReservation,
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

  eventTimesChanged({ event, newStart, newEnd }: CalendarEventTimesChangedEvent): void {
    this.events = this.events.map((iEvent) => {
      if (iEvent === event) {
        return {
          ...event,
          start: newStart,
          end: newEnd,
        };
      }
      return iEvent;
    });
    this.handleEvent('Dropped or resized', event);
  }

  handleEvent(action: string, event: CalendarEvent): void {
    this.modalData = { event, action };
    this.modal.open(this.modalContent, { size: 'lg' });
  }

  addEvent(): void {
    this.events = [
      ...this.events,
      {
        title: 'New event',
        start: new Date(),
        end: new Date(),
        draggable: true,
        resizable: {
          beforeStart: true,
          afterEnd: true,
        },
      },
    ];
  }

  deleteEvent(eventToDelete: CalendarEvent) {
    this.events = this.events.filter((event) => event !== eventToDelete);
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

    // FIXME
    /*
    this.reservation.startAt = new Date();
    this.reservation.finishAt = new Date();
    console.log(this.reservation);
    this.submitReservation.emit(this.reservation);
		*/
  }

  handleSubmitReservation(): void {
    this.matDialog.closeAll();
  }

  handleCancelSubmit(): void {
    this.matDialog.closeAll();
  }
}
