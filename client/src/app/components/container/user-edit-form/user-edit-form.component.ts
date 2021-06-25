import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';

import { UserService } from 'src/app/shared/services/user.service';
import { AlertService } from 'src/app/shared/services/alert.service';
import { UserModel } from 'src/app/model/user-model';
import { UserUpdateRequest } from 'src/app/request/user-update-request';

@Component({
  selector: 'app-user-edit-form',
  templateUrl: './user-edit-form.component.html',
  styleUrls: ['./user-edit-form.component.css'],
})
export class UserEditFormComponent implements OnInit {
  user!: UserModel;
  hide = true;

  constructor(
    private userService: UserService,
    private alertService: AlertService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    // ユーザID
    const userId = Number(this.route.snapshot.paramMap.get('userId'));

    // 編集対象ユーザを取得
    const user: UserModel | undefined = this.userService.selectById(userId);
    if (user === undefined) {
      this.router.navigate(['/admin', 'users']);
      this.alertService.openSnackBar('ユーザが見つかりません', 'ERROR');
    } else {
      this.user = user;
    }
  }

  handleGoBack(): void {
    this.router.navigate(['/admin', 'users']);
  }

  handleSubmitUser(user: UserModel): void {
    // ユーザ更新リクエストを作成
    const requestBody: UserUpdateRequest = {
      firstName: user.firstName,
      lastName: user.lastName,
      email: user.email,
      password: user.password,
      roleId: user.roleId,
      admissionYear: user.admissionYear,
    };

    // リクエスト送信
    this.userService.updateUser(user.id, requestBody).subscribe(
      () => {
        this.handleGoBack();
        this.alertService.openSnackBar('ユーザを更新しました', 'SUCCESS');
      },
      (error) => {
        this.alertService.openSnackBar(error, 'ERROR');
      }
    );
  }
}
