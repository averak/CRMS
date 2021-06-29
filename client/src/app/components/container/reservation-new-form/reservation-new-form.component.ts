import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-reservation-new-form',
  templateUrl: './reservation-new-form.component.html',
  styleUrls: ['./reservation-new-form.component.css'],
})
export class ReservationNewFormComponent implements OnInit {
  constructor() {}

  ngOnInit(): void {}

  handleSubmitReservation(): void {
    // FIXME
    console.log('追加しました');
  }

  handleCancelSubmit(): void {
    // FIXME
    console.log('キャンセルしました');
  }
}
