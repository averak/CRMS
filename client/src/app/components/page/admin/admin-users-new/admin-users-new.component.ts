import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { AlertService } from 'src/app/shared/services/alert.service';
import { UserService } from 'src/app/shared/services/user.service';
import { UserCreateRequest } from 'src/app/request/user-create-request';

@Component({
  selector: 'app-admin-users-new',
  templateUrl: './admin-users-new.component.html',
  styleUrls: ['./admin-users-new.component.css'],
})
export class AdminUsersNewComponent implements OnInit {
  userCreateRequest!: UserCreateRequest;
  hide = true;
  admissionYears!: number[];

  constructor(
    private alertService: AlertService,
    private userService: UserService,
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
    const currentYear: number = new Date().getFullYear();
    this.admissionYears = [...Array(8)].map((_: undefined, idx: number) => idx + currentYear - 7);
  }

  onSubmit(): void {
    this.userService.createUser(this.userCreateRequest).subscribe(
      () => {
        this.router.navigate(['/admin/users']);
        this.alertService.openSnackBar('ユーザを新規作成しました', 'SUCCESS');
      },
      (error) => {
        this.router.navigate(['/error'], { queryParams: { status_code: error.status } });
        this.alertService.openSnackBar(error, 'ERROR');
      }
    );
  }
}
