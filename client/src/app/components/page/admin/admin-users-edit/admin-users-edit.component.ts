import { Component, OnInit, HostListener } from '@angular/core';

import { OnBeforeUnload } from 'src/app/shared/guards/before-unload.guard';

@Component({
  selector: 'app-admin-users-edit',
  templateUrl: './admin-users-edit.component.html',
  styleUrls: ['./admin-users-edit.component.css'],
})
export class AdminUsersEditComponent implements OnInit, OnBeforeUnload {
  shouldConfirmOnBeforeunload = true;

  constructor() {}

  ngOnInit(): void {}

  @HostListener('window:beforeunload', ['$event'])
  beforeUnload(e: Event) {
    if (this.shouldConfirmOnBeforeunload) {
      e.returnValue = true;
    }
  }
}
