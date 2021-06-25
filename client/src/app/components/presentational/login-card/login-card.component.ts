import { Component, OnInit, Output, EventEmitter } from '@angular/core';

import { UserLoginRequest } from 'src/app/request/user-login-request';

@Component({
  selector: 'app-login-card',
  templateUrl: './login-card.component.html',
  styleUrls: ['./login-card.component.css'],
})
export class LoginCardComponent implements OnInit {
  @Output() submitLogin: EventEmitter<UserLoginRequest> = new EventEmitter<UserLoginRequest>();

  userLoginRequest!: UserLoginRequest;
  hide = true;

  constructor() {}

  ngOnInit(): void {
    this.userLoginRequest = {
      email: undefined,
      password: undefined,
    };
  }

  onSubmit(): void {
    this.submitLogin.emit(this.userLoginRequest);
  }
}
