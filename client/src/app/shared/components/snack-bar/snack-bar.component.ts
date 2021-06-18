import { Component, OnInit, Inject } from '@angular/core';
import { MAT_SNACK_BAR_DATA, MatSnackBarRef } from '@angular/material/snack-bar';

interface SnackBarData {
  message: string;
  level: 'SUCCESS' | 'INFO' | 'ERROR' | 'WARN';
}

@Component({
  selector: 'app-snack-bar',
  templateUrl: './snack-bar.component.html',
  styleUrls: ['./snack-bar.component.css'],
})
export class SnackBarComponent implements OnInit {
  constructor(
    public snackBarRef: MatSnackBarRef<SnackBarComponent>,
    @Inject(MAT_SNACK_BAR_DATA) public data: SnackBarData
  ) {}

  ngOnInit(): void {}

  dismiss() {
    this.snackBarRef.dismiss();
  }

  get getIcon(): string {
    switch (this.data.level) {
      case 'SUCCESS':
        return 'check_circle';
      case 'INFO':
        return 'info';
      case 'ERROR':
        return 'error';
      case 'WARN':
        return 'warning';
    }
  }
}
