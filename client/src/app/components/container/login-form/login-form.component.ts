import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';

import { environment } from 'src/environments/environment';
import { AuthService } from 'src/app/shared/services/auth.service';
import { AlertService } from 'src/app/shared/services/alert.service';
import { UserLoginRequest } from 'src/app/request/user-login-request';

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.css'],
})
export class LoginFormComponent implements OnInit {
  @Output() loginTransit: EventEmitter<any> = new EventEmitter<any>();

  hide = true;

  constructor(
    private authService: AuthService,
    private alertService: AlertService,
    private cookieService: CookieService
  ) {}

  ngOnInit(): void {}

  handleLogin(requestBody: UserLoginRequest) {
    this.authService.login(requestBody).subscribe(
      (accessToken) => {
        this.authService.setCredentials(accessToken);
        this.alertService.openSnackBar('ログインに成功しました', 'SUCCESS');
        this.loginTransit.emit();
      },
      (error) => {
        this.authService.logout();
        this.alertService.openSnackBar(error, 'ERROR');
      }
    );
  }
}
