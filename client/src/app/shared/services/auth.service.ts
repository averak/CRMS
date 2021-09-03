import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CookieService } from 'ngx-cookie-service';
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { environment } from 'src/environments/environment';
import { UserLoginRequest } from 'src/app/request/user-login-request';
import { AccessTokenModel } from 'src/app/model/user-model';
import { ErrorMessageService } from 'src/app/shared/services/error-message.service';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(
    private router: Router,
    private http: HttpClient,
    private errorMessageService: ErrorMessageService,
    private cookieService: CookieService
  ) {}

  public login(requestBody: UserLoginRequest): Observable<AccessTokenModel> {
    // request options
    const options = {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
    };

    return this.http
      .post<AccessTokenModel>(`${environment.API_PREFIX}/api/login`, requestBody, options)
      .pipe(
        catchError((error) => {
          this.logout();
          throw this.errorMessageService.getErrorMessage(error.error.code);
        })
      );
  }

  public logout(): void {
    this.router.navigate(['/login']);
    this.cookieService.deleteAll();
  }

  public checkAuthenticated(): boolean {
    return this.cookieService.check(environment.CREDENTIALS_KEY);
  }

  public getCredentials(): string {
    return this.cookieService.get(environment.CREDENTIALS_KEY);
  }

  public setCredentials(accessToken: AccessTokenModel): void {
    this.cookieService.set(
      environment.CREDENTIALS_KEY,
      `${accessToken.tokenType} ${accessToken.accessToken}`
    );
  }
}
