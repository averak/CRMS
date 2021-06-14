import { Component, OnInit } from '@angular/core';
import { UserLoginRequest } from 'src/app/model/UserLoginRequest';

@Component({
  selector: 'app-login-card',
  templateUrl: './login-card.component.html',
  styleUrls: ['./login-card.component.css'],
})
export class LoginCardComponent implements OnInit {
  user!: UserLoginRequest;
  hide = true;

  constructor() {}

  ngOnInit(): void {
    this.user = {
      email: '',
      password: '',
    };
  }

  onSubmit() {
    console.log(this.user);
  }
}
