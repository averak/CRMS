import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { AlertService } from 'src/app/shared/services/alert.service';
import { UserService } from 'src/app/shared/services/user.service';
import { AdmissionYearService } from 'src/app/shared/services/admission-year.service';
import { UserCreateRequest } from 'src/app/request/user-create-request';

@Component({
  selector: 'app-user-new-form',
  templateUrl: './user-new-form.component.html',
  styleUrls: ['./user-new-form.component.css'],
})
export class UserNewFormComponent implements OnInit {
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
