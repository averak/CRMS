import { Component, OnInit } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';

import { UserLoginRequest } from 'src/app/model/UserLoginRequest';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {
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
