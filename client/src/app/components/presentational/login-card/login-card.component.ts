import { Component, OnInit } from '@angular/core';

import { environment } from 'src/environments/environment';
import { AuthService } from 'src/app/shared/services/auth.service';
import { AlertService } from 'src/app/shared/services/alert.service';
import { UserLoginRequest } from 'src/app/request/user-login-request';

@Component({
  selector: 'app-login-card',
  templateUrl: './login-card.component.html',
  styleUrls: ['./login-card.component.css'],
})
export class LoginCardComponent implements OnInit {
  userLoginRequest!: UserLoginRequest;
  hide = true;

  constructor(private authService: AuthService, private alertService: AlertService) {}

  ngOnInit(): void {
    this.userLoginRequest = {
      email: '',
      password: '',
    };
  }

  onSubmit() {
    this.authService.login(this.userLoginRequest).subscribe(
      (resp) => {
        localStorage.setItem(environment.LOCAL_STORAGE_AUTH_KEY, resp.headers.get('Authorization'));
        this.alertService.openSnackBar('ログインに成功しました', 'SUCCESS');
      },
      (error) => {
        localStorage.removeItem(environment.LOCAL_STORAGE_AUTH_KEY);
        this.alertService.openSnackBar(error, 'ERROR');
      }
    );
  }
}
