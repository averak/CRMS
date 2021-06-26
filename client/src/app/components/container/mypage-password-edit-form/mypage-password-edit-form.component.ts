import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { UserService } from 'src/app/shared/services/user.service';
import { AlertService } from 'src/app/shared/services/alert.service';
import { UserModel } from 'src/app/model/user-model';
import { LoginUserPasswordUpdateRequest } from 'src/app/request/login-user-password-update-request';

@Component({
  selector: 'app-mypage-password-edit-form',
  templateUrl: './mypage-password-edit-form.component.html',
  styleUrls: ['./mypage-password-edit-form.component.css'],
})
export class MypagePasswordEditFormComponent implements OnInit {
  user!: UserModel;

  constructor(
    private userService: UserService,
    private alertService: AlertService,
    private router: Router
  ) {}

  ngOnInit(): void {
    // ログインユーザを取得
    this.user = this.userService.loginUser;
  }

  handleGoBack(): void {
    this.router.navigate(['/dashboard']);
  }

  handleSubmitUser(user: UserModel): void {
    // ログインユーザのパスワード更新リクエストを作成
    const requestBody: LoginUserPasswordUpdateRequest = {
      currentPassword: user.currentPassword,
      newPassword: user.newPassword,
    };

    this.userService.updateLoginUserPassword(requestBody).subscribe(
      () => {
        this.handleGoBack();
        this.alertService.openSnackBar('パスワードを更新しました', 'SUCCESS');

        // reload login user
        this.userService.getLoginUser().subscribe(
          (user: UserModel) => {
            this.user = user;
            this.userService.setLoginUser(this.user);
          },
          (error) => {
            this.alertService.openSnackBar(error, 'ERROR');
          }
        );
      },
      (error) => {
        this.alertService.openSnackBar(error, 'ERROR');
      }
    );
  }
}
