import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

import { SnackBarComponent } from 'src/app/shared/components/snack-bar/snack-bar.component';

@Injectable({
  providedIn: 'root',
})
export class AlertService {
  constructor(private snackBar: MatSnackBar) {}

  openSnackBar(message: string, type: 'SUCCESS' | 'INFO' | 'WARN' | 'ERROR'): void {
    const duration = type == 'SUCCESS' || type == 'INFO' ? 5000 : -1;

    this.snackBar.openFromComponent(SnackBarComponent, {
      duration: duration,
      data: { message: message, level: type },
    });
  }
}
