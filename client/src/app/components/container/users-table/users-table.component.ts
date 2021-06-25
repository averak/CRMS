import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';

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
  columns: string[] = ['name', 'email', 'admissionYear', 'userRole', 'buttons'];
  dataSource!: MatTableDataSource<UserModel>;
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(
    private userService: UserService,
    private alertService: AlertService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.userService.getUsers().subscribe(
      (users: UsersModel) => {
        this.users = users.users;
        this.userService.setUsers(this.users);

        // 入学年度/IDでソート
        this.users.sort((a, b) => {
          if (a.admissionYear > b.admissionYear) return 1;
          if (a.admissionYear < b.admissionYear) return -1;
          return a.lastName.localeCompare(b.lastName, 'ja');
        });

        this.dataSource = new MatTableDataSource<UserModel>(this.users);
        this.dataSource.paginator = this.paginator;
      },
      (error) => {
        this.alertService.openSnackBar(error, 'ERROR');
      }
    );
  }

  onEditClick(user: UserModel): void {
    this.router.navigate(['/admin', 'users', user.id, 'edit']);
  }

  onDeleteClick(user: UserModel): void {
    this.userService.deleteUser(user.id).subscribe(
      () => {
        this.loadUsers();
        this.alertService.openSnackBar('ユーザを削除しました', 'SUCCESS');
      },
      (error) => {
        this.alertService.openSnackBar(error, 'ERROR');
      }
    );
  }
}
