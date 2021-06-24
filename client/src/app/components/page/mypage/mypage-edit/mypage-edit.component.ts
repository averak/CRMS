import { Component, OnInit, HostListener } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';

import { UserService } from 'src/app/shared/services/user.service';
import { AlertService } from 'src/app/shared/services/alert.service';
import { AdmissionYearService } from 'src/app/shared/services/admission-year.service';
import { UserModel } from 'src/app/model/user-model';
import { LoginUserUpdateRequest } from 'src/app/request/login-user-update-request';
import { OnBeforeUnload } from 'src/app/shared/guards/before-unload.guard';

@Component({
  selector: 'app-mypage-edit',
  templateUrl: './mypage-edit.component.html',
  styleUrls: ['./mypage-edit.component.css'],
})
export class MypageEditComponent implements OnInit, OnBeforeUnload {
  userId!: number;
  user!: UserModel;
  loginUserUpdateRequest!: LoginUserUpdateRequest;
  admissionYears!: number[];
  hideCurrentPassword = true;
  hideNewPassword = true;

  shouldConfirmOnBeforeunload = true;

  constructor(
    private userService: UserService,
    private alertService: AlertService,
    private router: Router,
    private route: ActivatedRoute
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
  }

  @HostListener('window:beforeunload', ['$event'])
  beforeUnload(e: Event) {
    if (this.shouldConfirmOnBeforeunload) {
      e.returnValue = true;
    }
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
