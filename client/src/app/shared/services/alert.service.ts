import { Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';

import { ConfirmDialogComponent } from 'src/app/shared/components/confirm-dialog/confirm-dialog.component';
import { SnackBarComponent } from 'src/app/shared/components/snack-bar/snack-bar.component';

@Injectable({
  providedIn: 'root',
})
export class AlertService {
  constructor(public matDialog: MatDialog, private snackBar: MatSnackBar) {}

  openSnackBar(message: string, type: 'SUCCESS' | 'INFO' | 'WARN' | 'ERROR'): void {
    const duration = type == 'ERROR' ? -1 : 5000;

    this.snackBar.openFromComponent(SnackBarComponent, {
      duration: duration,
      data: { message: message, level: type },
    });
  }

  confirmDialog(title: string, message: string, callback: (result: boolean) => void): void {
    const dialog = this.matDialog.open(ConfirmDialogComponent, {
      data: {
        title: title,
        message: message,
      },
    });
    dialog.afterClosed().subscribe((result: string) => {
      callback(result === 'OK');
    });
  }
}
