import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MatTableDataSource } from '@angular/material/table';

import { UserService } from 'src/app/shared/services/user.service';
import { AlertService } from 'src/app/shared/services/alert.service';
import { UsersModel } from 'src/app/model/users-model';
import { UserModel } from 'src/app/model/user-model';

@Component({
  selector: 'app-users-table',
  templateUrl: './users-table.component.html',
  styleUrls: ['./users-table.component.css'],
})
export class UsersTableComponent implements OnInit {
  users: UserModel[] = [];
  columns: string[] = ['name', 'email', 'admissionAt', 'userRole', 'buttons'];
  dataSource!: MatTableDataSource<UserModel>;

  constructor(
    private userService: UserService,
    private alertService: AlertService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.userService.getUsers().subscribe(
      (users: UsersModel) => {
        this.users = users.users;

        // IDでソート
        this.users.sort((a, b) => {
          if (a.id > b.id) {
            return 1;
          } else {
            return -1;
          }
        });

        this.dataSource = new MatTableDataSource<UserModel>(this.users);
      },
      (error) => {
        this.router.navigate(['/error'], { queryParams: { status_code: error.status } });
        this.alertService.openSnackBar(error, 'ERROR');
      }
    );
  }
}
