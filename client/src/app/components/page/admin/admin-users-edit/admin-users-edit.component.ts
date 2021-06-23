import { Component, OnInit, Input } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';

import { UserService } from 'src/app/shared/services/user.service';
import { AlertService } from 'src/app/shared/services/alert.service';
import { AdmissionYearService } from 'src/app/shared/services/admission-year.service';
import { UserModel } from 'src/app/model/user-model';
import { UserUpdateRequest } from 'src/app/request/user-update-request';

@Component({
  selector: 'app-admin-users-edit',
  templateUrl: './admin-users-edit.component.html',
  styleUrls: ['./admin-users-edit.component.css'],
})
export class AdminUsersEditComponent implements OnInit {
  userId!: number;
  user!: UserModel;
  userUpdateRequest!: UserUpdateRequest;
  admissionYears!: number[];
  hide = true;

  constructor(
    private userService: UserService,
    private alertService: AlertService,
    private admissionYearService: AdmissionYearService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    // ユーザID
    this.userId = Number(this.route.snapshot.paramMap.get('userId'));

    // 編集対象ユーザを取得
    const user: UserModel | undefined = this.userService.selectById(this.userId);
    if (user === undefined) {
      this.router.navigate(['/admin', 'users']);
      this.alertService.openSnackBar('ユーザが見つかりません', 'ERROR');
    } else {
      this.user = user;
    }

    // init request body
    this.userUpdateRequest = {
      firstName: this.user.firstName,
      lastName: this.user.lastName,
      email: this.user.email,
      password: undefined,
      admissionYear: this.user.admissionYear,
      roleId: this.user.roleId,
    };

    // 入学年度リストを作成
    this.admissionYears = this.admissionYearService.getAdmissionYears();
  }

  onGoBack(): void {
    this.router.navigate(['/admin', 'users']);
  }

  onSubmit(): void {
    this.userService.updateUser(this.userId, this.userUpdateRequest).subscribe(
      () => {
        this.router.navigate(['/admin/users']);
        this.alertService.openSnackBar('ユーザを更新しました', 'SUCCESS');
      },
      (error) => {
        this.alertService.openSnackBar(error, 'ERROR');
      }
    );
  }
}
