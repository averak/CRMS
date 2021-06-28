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
import {
  startOfDay,
  endOfDay,
  subDays,
  addDays,
  endOfMonth,
  isSameDay,
  isSameMonth,
  addHours,
} from 'date-fns';
import { Subject } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import {
  CalendarEvent,
  CalendarEventAction,
  CalendarEventTimesChangedEvent,
  CalendarView,
} from 'angular-calendar';
import { MatDialog } from '@angular/material/dialog';

import { ReservationModel } from 'src/app/model/reservation-model';
import { ReservationCreateRequest } from 'src/app/request/reservation-create-request';
import { ReservationNewFormComponent } from 'src/app/components/container/reservation-new-form/reservation-new-form.component';

@Component({
  selector: 'app-reservations-calendar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './reservations-calendar.component.html',
  styleUrls: ['./reservations-calendar.component.css'],
})
export class ReservationsCalendarComponent implements OnInit {
  @Input() reservations!: ReservationModel[];
  @Output() submitReservation: EventEmitter<any> = new EventEmitter<any>();

  @ViewChild('modalContent', { static: true }) modalContent!: TemplateRef<any>;

  view: CalendarView = CalendarView.Week;
  viewDate: Date = new Date();

  events!: CalendarEvent[];

  modalData!: {
    action: string;
    event: CalendarEvent;
  };

  constructor(private modal: NgbModal, private matDialog: MatDialog) {}

  ngOnInit(): void {
    // イベント一覧
    this.events = this.reservations.map((reservation: ReservationModel) => {
      return {
        start: new Date(reservation.startAt),
        end: new Date(reservation.finishAt),
        title: `${reservation.user.lastName} ${reservation.user.firstName}`,
        // FIXME: 自分の予約の場合にはtrue
        resizable: {
          beforeStart: false,
          afterEnd: false,
        },
        draggable: false,
      };
    });

    console.log(this.events[0]);
  }

  actions: CalendarEventAction[] = [
    {
      label: '<i class="fas fa-fw fa-pencil-alt"></i>',
      a11yLabel: 'Edit',
      onClick: ({ event }: { event: CalendarEvent }): void => {
        this.handleEvent('Edited', event);
      },
    },
    {
      label: '<i class="fas fa-fw fa-trash-alt"></i>',
      a11yLabel: 'Delete',
      onClick: ({ event }: { event: CalendarEvent }): void => {
        this.events = this.events.filter((iEvent) => iEvent !== event);
        this.handleEvent('Deleted', event);
      },
    },
  ];

  refresh: Subject<any> = new Subject();
  activeDayIsOpen: boolean = true;

  dayClicked({ date, events }: { date: Date; events: CalendarEvent[] }): void {
    if (isSameMonth(date, this.viewDate)) {
      if (
        (isSameDay(this.viewDate, date) && this.activeDayIsOpen === true) ||
        events.length === 0
      ) {
        this.activeDayIsOpen = false;
      } else {
        this.activeDayIsOpen = true;
      }
      this.viewDate = date;
    }
  }

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
        start: startOfDay(new Date()),
        end: endOfDay(new Date()),
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
    this.matDialog.open(ReservationNewFormComponent, {
      width: '640px',
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
