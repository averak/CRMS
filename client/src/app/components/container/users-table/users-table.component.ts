import { Component, OnInit, EventEmitter, Input, Output, ViewChild } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';

import { UserModel } from 'src/app/model/user-model';

@Component({
  selector: 'app-users-table',
  templateUrl: './users-table.component.html',
  styleUrls: ['./users-table.component.css'],
})
export class UsersTableComponent implements OnInit {
  @Input() users!: UserModel[];
  @Output() userEditTransit: EventEmitter<any> = new EventEmitter<any>();
  @Output() userDeleteTransit: EventEmitter<any> = new EventEmitter<any>();

  columns: string[] = ['name', 'email', 'admissionYear', 'userRole', 'buttons'];
  dataSource!: MatTableDataSource<UserModel>;
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor() {}

  ngOnInit(): void {
    this.dataSource = new MatTableDataSource<UserModel>(this.users);
    this.dataSource.paginator = this.paginator;
  }

  onEditClick(user: UserModel): void {
    this.userEditTransit.emit(user);
  }

  onDeleteClick(user: UserModel): void {
    this.userDeleteTransit.emit(user);
  }
}
