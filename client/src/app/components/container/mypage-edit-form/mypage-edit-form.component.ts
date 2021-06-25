import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { UserService } from 'src/app/shared/services/user.service';
import { AlertService } from 'src/app/shared/services/alert.service';
import { AdmissionYearService } from 'src/app/shared/services/admission-year.service';
import { UserModel } from 'src/app/model/user-model';
import { LoginUserUpdateRequest } from 'src/app/request/login-user-update-request';

@Component({
  selector: 'app-mypage-edit-form',
  templateUrl: './mypage-edit-form.component.html',
  styleUrls: ['./mypage-edit-form.component.css'],
})
export class MypageEditFormComponent implements OnInit {
  userId!: number;
  user!: UserModel;
  loginUserUpdateRequest!: LoginUserUpdateRequest;
  admissionYears!: number[];
  hideCurrentPassword = true;
  hideNewPassword = true;

  constructor(
    private userService: UserService,
    private alertService: AlertService,
    private admissionYearService: AdmissionYearService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.user = this.userService.loginUser;

    // init request body
    this.loginUserUpdateRequest = {
      firstName: this.user.firstName,
      lastName: this.user.lastName,
      email: this.user.email,
      newPassword: undefined,
      currentPassword: undefined,
    };

    // 入学年度リストを作成
    this.admissionYears = this.admissionYearService.getAdmissionYears();
  }

  onGoBack(): void {
    this.router.navigate(['/dashboard']);
  }

  onSubmit(): void {
    this.userService.updateLoginUser(this.loginUserUpdateRequest).subscribe(
      () => {
        this.router.navigate(['/dashboard']);
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
