import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { UserService } from 'src/app/shared/services/user.service';
import { AlertService } from 'src/app/shared/services/alert.service';
import { UserModel } from 'src/app/model/user-model';
import { LoginUserUpdateRequest } from 'src/app/request/login-user-update-request';

@Component({
  selector: 'app-mypage-profile-edit-form',
  templateUrl: './mypage-profile-edit-form.component.html',
  styleUrls: ['./mypage-profile-edit-form.component.css'],
})
export class MypageProfileEditFormComponent implements OnInit {
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

  onClickDisabledColumn(): void {
    this.alertService.openSnackBar('この項目の編集は禁止されています', 'WARN');
  }

  handleGoBack(): void {
    this.router.navigate(['/dashboard']);
  }

  handleSubmitUser(user: UserModel): void {
    // ログインユーザ更新リクエストを作成
    const requestBody: LoginUserUpdateRequest = {
      firstName: user.firstName,
      lastName: user.lastName,
      email: user.email,
    };

    this.userService.updateLoginUser(requestBody).subscribe(
      () => {
        this.handleGoBack();
        this.alertService.openSnackBar('プロフィールを更新しました', 'SUCCESS');

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
