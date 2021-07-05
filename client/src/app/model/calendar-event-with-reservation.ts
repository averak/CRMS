import { CalendarEvent, CalendarEventAction } from 'angular-calendar';

import { ReservationModel } from './reservation-model';

export class CalendarEventWithReservation implements CalendarEvent {
  reservation!: ReservationModel;
  start!: Date;
  end!: Date;
  title!: string;
  color!: { primary: string; secondary: string };
  actions!: CalendarEventAction[];
  resizable!: { beforeStart: boolean; afterEnd: boolean };
  draggable!: boolean;
}
