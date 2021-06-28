import { Component, OnInit, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-reservation-new-card',
  templateUrl: './reservation-new-card.component.html',
  styleUrls: ['./reservation-new-card.component.css'],
})
export class ReservationNewCardComponent implements OnInit {
  @Output() submitReservation: EventEmitter<any> = new EventEmitter<any>();
  @Output() cancelSubmit: EventEmitter<any> = new EventEmitter<any>();

  constructor() {}

  ngOnInit(): void {}

  onSubmit(): void {
    this.submitReservation.emit();
  }

  onCancel(): void {
    this.cancelSubmit.emit();
  }
}
