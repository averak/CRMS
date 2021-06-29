import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-reservation-new-dialog',
  templateUrl: './reservation-new-dialog.component.html',
  styleUrls: ['./reservation-new-dialog.component.css'],
})
export class ReservationNewDialogComponent implements OnInit {
  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any,
    private matDialogRef: MatDialogRef<ReservationNewDialogComponent>
  ) {}

  ngOnInit(): void {}

  handleSubmitReservation(): void {
    // FIXME
    console.log('追加しました');
  }

  handleCancelSubmit(): void {
    this.matDialogRef.close('cancel');
  }
}
