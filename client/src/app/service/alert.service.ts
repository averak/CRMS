import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root',
})
export class AlertService {
  constructor(private snackBar: MatSnackBar) {}

  openSuccessSnackBar(message: string, duration: number = 5000): void {
    this.snackBar.open(message, 'close', { duration });
  }

  openErrorSnackBar(message: string, duration: number = -1): void {
    this.snackBar.open(message, 'close', { duration });
  }
}
