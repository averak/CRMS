import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

import { environment } from 'src/environments/environment';
import { HttpBaseService } from 'src/app/shared/services/http-base.service';
import { UserModel } from 'src/app/model/user-model';
import { UsersModel } from 'src/app/model/users-model';
import { UserRoleEnum } from 'src/app/enums/user-role-enum';
import { UserCreateRequest } from 'src/app/request/user-create-request';
import { UserUpdateRequest } from 'src/app/request/user-update-request';
import { LoginUserUpdateRequest } from 'src/app/request/login-user-update-request';
import { LoginUserPasswordUpdateRequest } from 'src/app/request/login-user-password-update-request';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  users!: UserModel[];
  loginUser: BehaviorSubject<UserModel> = new BehaviorSubject<UserModel>({} as UserModel);

  constructor(private httpBaseService: HttpBaseService) {
    this.users = [];
  }

  getLoginUser(): Observable<UserModel> {
    if (Object.keys(this.loginUser.getValue()).length === 0) {
      this.getSessionUser().subscribe(
        (user: UserModel) => {
          this.loginUser.next(user);
        },
        (error) => this.loginUser.error(error)
      );
    }

    return this.loginUser;
  }

  getSessionUser(): Observable<UserModel> {
    return this.httpBaseService.getRequest<UserModel>(`${environment.API_PREFIX}/api/users/me`);
  }

  getUsers(): Observable<UsersModel> {
    return this.httpBaseService.getRequest<UsersModel>(`${environment.API_PREFIX}/api/users`);
  }

  setUsers(users: UserModel[]): void {
    this.users = users;
  }

  selectById(userId: number): UserModel | undefined {
    return this.users.find((user) => user.id == userId);
  }

  createUser(requestBody: UserCreateRequest) {
    return this.httpBaseService.postRequest<UserModel>(
      `${environment.API_PREFIX}/api/users`,
      requestBody
    );
  }

  updateUser(userId: number, requestBody: UserUpdateRequest): Observable<any> {
    return this.httpBaseService.putRequest<UserModel>(
      `${environment.API_PREFIX}/api/users/${userId}`,
      requestBody
    );
  }

  updateLoginUser(requestBody: LoginUserUpdateRequest): Observable<any> {
    return this.httpBaseService.putRequest<UserModel>(
      `${environment.API_PREFIX}/api/users/me`,
      requestBody
    );
  }

  updateLoginUserPassword(requestBody: LoginUserPasswordUpdateRequest): Observable<any> {
    return this.httpBaseService.putRequest<UserModel>(
      `${environment.API_PREFIX}/api/users/me/password`,
      requestBody
    );
  }

  deleteUser(userId: number): Observable<any> {
    return this.httpBaseService.deleteRequest<UserModel>(
      `${environment.API_PREFIX}/api/users/${userId}`
    );
  }

  checkAdmin(user: UserModel): boolean {
    return user.roleId == UserRoleEnum.ADMIN;
  }

  getUserName(user: UserModel): string {
    return `${user.lastName} ${user.firstName}`;
  }

  sortUsers(users: UserModel[]): UserModel[] {
    // 入学年度/IDでソート
    users.sort((a, b) => {
      if (a.admissionYear > b.admissionYear) return 1;
      if (a.admissionYear < b.admissionYear) return -1;
      return a.lastName.localeCompare(b.lastName, 'ja');
    });
    return users;
  }
}
