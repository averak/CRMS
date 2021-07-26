import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { UserService } from 'src/app/shared/services/user.service';
import { AlertService } from 'src/app/shared/services/alert.service';
import { UserModel } from 'src/app/model/user-model';
import { UserCreateRequest } from 'src/app/request/user-create-request';

@Component({
  selector: 'app-user-new-form',
  templateUrl: './user-new-form.component.html',
  styleUrls: ['./user-new-form.component.css'],
})
export class UserNewFormComponent implements OnInit {
  hide = true;

  constructor(
    private alertService: AlertService,
    private userService: UserService,
    private router: Router
  ) {}

  ngOnInit(): void {}

  handleGoBack(): void {
    this.router.navigate(['/admin', 'users']);
  }

  handleSubmitUser(user: UserModel): void {
    // ユーザ作成リクエストを作成
    const requestBody: UserCreateRequest = {
      firstName: user.firstName,
      lastName: user.lastName,
      email: user.email,
      password: user.password,
      roleId: user.roleId,
      admissionYear: user.admissionYear,
    };

    // リクエスト送信
    this.userService.createUser(requestBody).subscribe(
      () => {
        this.userService.fetchUsers();
        this.handleGoBack();
        this.alertService.openSnackBar('ユーザを新規作成しました', 'SUCCESS');
      },
      (error) => {
        this.alertService.openSnackBar(error, 'ERROR');
      }
    );
  }
}
