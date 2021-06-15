import { Component, OnInit } from '@angular/core';

import { AlertService } from 'src/app/service/alert.service';
import { UserLoginRequest } from 'src/app/model/user-login-request';

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
    this.alertService.openErrorSnackBar('ログインに失敗しました');
  }
}
