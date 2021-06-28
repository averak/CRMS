import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError } from 'rxjs/operators';

import { AuthService } from './auth.service';
import { ErrorMessageService } from './error-message.service';

@Injectable({
  providedIn: 'root',
})
export class HttpBaseService {
  constructor(
    private router: Router,
    private http: HttpClient,
    private authService: AuthService,
    private errorMessageService: ErrorMessageService
  ) {}

  getRequest<T>(url: string, redirectErrorPage: boolean = false) {
    const options = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        Authorization: this.authService.getJwt(),
      }),
    };

    return this.http.get<T>(url, options).pipe(
      catchError((error) => {
        const errorCodes: number[] = Object.keys(this.errorMessageService.messages).map(Number);

        // エラーページ
        if (redirectErrorPage && errorCodes.indexOf(error.status) >= 0) {
          this.router.navigate(['/error'], { queryParams: { status_code: error.status } });
        }

        // 無効なJWT
        if (error.status == 401) {
          this.authService.logout();
        }

        throw this.errorMessageService.getErrorMessage(error.error.code);
      })
    );
  }

  postRequest<T>(url: string, requestBody: any, redirectErrorPage: boolean = false) {
    const options = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        Authorization: this.authService.getJwt(),
      }),
    };

    return this.http.post<T>(url, requestBody, options).pipe(
      catchError((error) => {
        const errorCodes: number[] = Object.keys(this.errorMessageService.messages).map(Number);

        // エラーページ
        if (redirectErrorPage && errorCodes.indexOf(error.status) >= 0) {
          this.router.navigate(['/error'], { queryParams: { status_code: error.status } });
        }

        // 無効なJWT
        if (error.status == 401) {
          this.authService.logout();
        }

        throw this.errorMessageService.getErrorMessage(error.error.code);
      })
    );
  }

  putRequest<T>(url: string, requestBody: any, redirectErrorPage: boolean = false) {
    const options = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        Authorization: this.authService.getJwt(),
      }),
    };

    return this.http.put<T>(url, requestBody, options).pipe(
      catchError((error) => {
        const errorCodes: number[] = Object.keys(this.errorMessageService.messages).map(Number);

        // エラーページ
        if (redirectErrorPage && errorCodes.indexOf(error.status) >= 0) {
          this.router.navigate(['/error'], { queryParams: { status_code: error.status } });
        }

        // 無効なJWT
        if (error.status == 401) {
          this.authService.logout();
        }

        throw this.errorMessageService.getErrorMessage(error.error.code);
      })
    );
  }

  deleteRequest<T>(url: string, redirectErrorPage: boolean = false) {
    const options = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        Authorization: this.authService.getJwt(),
      }),
    };

    return this.http.delete<T>(url, options).pipe(
      catchError((error) => {
        const errorCodes: number[] = Object.keys(this.errorMessageService.messages).map(Number);

        // エラーページ
        if (redirectErrorPage && errorCodes.indexOf(error.status) >= 0) {
          this.router.navigate(['/error'], { queryParams: { status_code: error.status } });
        }

        // 無効なJWT
        if (error.status == 401) {
          this.authService.logout();
        }

        throw this.errorMessageService.getErrorMessage(error.error.code);
      })
    );
  }
}
