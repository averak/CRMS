import { Component, OnInit, Output, EventEmitter } from '@angular/core';

import { ReservationModel } from 'src/app/model/reservation-model';

@Component({
  selector: 'app-reservation-new-card',
  templateUrl: './reservation-new-card.component.html',
  styleUrls: ['./reservation-new-card.component.css'],
})
export class ReservationNewCardComponent implements OnInit {
  @Output() submitReservation: EventEmitter<any> = new EventEmitter<any>();
  @Output() cancelSubmit: EventEmitter<any> = new EventEmitter<any>();

  reservation: ReservationModel = {} as ReservationModel;

  constructor() {}

  ngOnInit(): void {}

  onSubmit(): void {
    this.submitReservation.emit(this.reservation);
  }

  onCancel(): void {
    this.cancelSubmit.emit();
  }
}
