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
  userCreateRequest!: UserCreateRequest;
  admissionYears!: number[];
  hide = true;

  shouldConfirmOnBeforeunload = true;

  constructor(
    private alertService: AlertService,
    private userService: UserService,
    private admissionYearService: AdmissionYearService,
    private router: Router
  ) {}

  ngOnInit(): void {
    // init request body
    this.userCreateRequest = {
      firstName: undefined,
      lastName: undefined,
      email: undefined,
      password: undefined,
      admissionYear: undefined,
      roleId: undefined,
    };

    // 入学年度リストを作成
    this.admissionYears = this.admissionYearService.getAdmissionYears();
  }

  @HostListener('window:beforeunload', ['$event'])
  beforeUnload(e: Event) {
    if (this.shouldConfirmOnBeforeunload) {
      e.returnValue = true;
    }
  }

  onGoBack(): void {
    this.router.navigate(['/admin', 'users']);
  }

  onSubmit(): void {
    this.userService.createUser(this.userCreateRequest).subscribe(
      () => {
        this.router.navigate(['/admin/users']);
        this.alertService.openSnackBar('ユーザを新規作成しました', 'SUCCESS');
      },
      (error) => {
        this.alertService.openSnackBar(error, 'ERROR');
      }
    );
  }
}
