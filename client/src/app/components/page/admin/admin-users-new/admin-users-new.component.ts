import { Component, OnInit } from '@angular/core';

import { UserCreateRequest } from 'src/app/request/user-create-request';

@Component({
  selector: 'app-admin-users-new',
  templateUrl: './admin-users-new.component.html',
  styleUrls: ['./admin-users-new.component.css'],
})
export class AdminUsersNewComponent implements OnInit {
  userCreateRequest!: UserCreateRequest;
  hide = true;
  admissionYears!: number[];

  constructor() {}

  ngOnInit(): void {
    // init request body
    this.userCreateRequest = {
      firstName: undefined,
      lastName: undefined,
      email: undefined,
      password: undefined,
      admissionYear: undefined,
      roleId: undefined,
    };

    // 入学年度リストを作成
    const currentYear: number = new Date().getFullYear();
    this.admissionYears = [...Array(8)].map((_: undefined, idx: number) => idx + currentYear - 7);
  }

  onSubmit(): void {
    console.log(this.userCreateRequest);
  }
}
