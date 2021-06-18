import { Component, OnInit } from '@angular/core';

import { AlertService } from 'src/app/shared/services/alert.service';
import { UserLoginRequest } from 'src/app/request/user-login-request';

@Component({
  selector: 'app-login-card',
  templateUrl: './login-card.component.html',
  styleUrls: ['./login-card.component.css'],
})
export class LoginCardComponent implements OnInit {
  user!: UserLoginRequest;
  hide = true;

  constructor(private alertService: AlertService) {}

  ngOnInit(): void {
    this.user = {
      email: '',
      password: '',
    };
  }

  onSubmit() {
    this.alertService.openSnackBar('ログインに失敗しました', 'ERROR');
  }
}
