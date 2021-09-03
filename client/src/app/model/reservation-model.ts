import { CalendarEvent, CalendarEventAction } from 'angular-calendar';

import { UserModel } from './user-model';

export interface ReservationModel {
  id: number;
  startAt: Date;
  finishAt: Date;
  user: UserModel;
}

export interface ReservationsModel {
  reservations: ReservationModel[];
}

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
