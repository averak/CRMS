import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { environment } from 'src/environments/environment';
import { HttpBaseService } from 'src/app/shared/services/http-base.service';
import { AuthService } from './auth.service';
import { ErrorMessageService } from './error-message.service';
import { UserModel } from 'src/app/model/user-model';
import { UsersModel } from 'src/app/model/users-model';
import { UserRoleEnum } from 'src/app/enums/user-role-enum';
import { UserCreateRequest } from 'src/app/request/user-create-request';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  constructor(
    private http: HttpClient,
    private httpBaseService: HttpBaseService,
    private authService: AuthService,
    private errorMessageService: ErrorMessageService
  ) {}

  getLoginUser(): Observable<UserModel> {
    return this.httpBaseService.getRequest<UserModel>(`${environment.API_PREFIX}/api/users/me`);
  }

  getUsers(): Observable<UsersModel> {
    return this.httpBaseService.getRequest<UsersModel>(`${environment.API_PREFIX}/api/users`);
  }

  createUser(requestBody: UserCreateRequest) {
    // request options
    const options = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        Authorization: this.authService.getJwt(),
      }),
    };

    return this.http.post<any>(`${environment.API_PREFIX}/api/users`, requestBody, options).pipe(
      catchError((error) => {
        throw this.errorMessageService.getErrorMessage(error.error.code);
      })
    );
  }

  deleteUser(userId: number): Observable<any> {
    // request options
    const options = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        Authorization: this.authService.getJwt(),
      }),
    };

    return this.http.delete<any>(`${environment.API_PREFIX}/api/users/${userId}`, options).pipe(
      catchError((error) => {
        console.log(error);
        throw this.errorMessageService.getErrorMessage(error.error.code);
      })
    );
  }

  checkAdmin(user: UserModel): boolean {
    return user.roleId == UserRoleEnum.ADMIN;
  }
}
