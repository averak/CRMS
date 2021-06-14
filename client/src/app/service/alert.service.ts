import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root',
})
export class AlertService {
  constructor(private snackBar: MatSnackBar) {}

  openSnackBar(message: string, duration: number = 3000): void {
    this.snackBar.open(message, 'close', { duration });
  }
}
