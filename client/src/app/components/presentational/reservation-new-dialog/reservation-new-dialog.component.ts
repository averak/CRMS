import { Component, OnInit, Output, EventEmitter } from '@angular/core';

import { ReservationModel } from 'src/app/model/reservation-model';

@Component({
  selector: 'app-reservation-new-dialog',
  templateUrl: './reservation-new-dialog.component.html',
  styleUrls: ['./reservation-new-dialog.component.css'],
})
export class ReservationNewDialogComponent implements OnInit {
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
