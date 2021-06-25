import { Component, OnInit, HostListener } from '@angular/core';
import { Router } from '@angular/router';

import { AlertService } from 'src/app/shared/services/alert.service';
import { UserService } from 'src/app/shared/services/user.service';
import { AdmissionYearService } from 'src/app/shared/services/admission-year.service';
import { UserCreateRequest } from 'src/app/request/user-create-request';
import { OnBeforeUnload } from 'src/app/shared/guards/before-unload.guard';

@Component({
  selector: 'app-admin-users-new',
  templateUrl: './admin-users-new.component.html',
  styleUrls: ['./admin-users-new.component.css'],
})
export class AdminUsersNewComponent implements OnInit, OnBeforeUnload {
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
