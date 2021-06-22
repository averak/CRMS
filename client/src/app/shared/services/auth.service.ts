import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { CookieService } from 'ngx-cookie-service';
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { environment } from 'src/environments/environment';
import { UserLoginRequest } from 'src/app/request/user-login-request';
import { ErrorMessageService } from 'src/app/shared/services/error-message.service';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(
    private http: HttpClient,
    private errorMessageService: ErrorMessageService,
    private cookieService: CookieService
  ) {}

  public login(requestBody: UserLoginRequest): Observable<any> {
    // request options
    const options = {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
      observe: 'response' as 'body',
    };

    return this.http.post<any>(`${environment.API_PREFIX}/api/login`, requestBody, options).pipe(
      catchError((error) => {
        this.logout();
        throw this.errorMessageService.getErrorMessage(error.error.code);
      })
    );
  }

  public logout(): void {
    this.cookieService.deleteAll();
  }

  public checkAuthenticated(): boolean {
    return this.cookieService.check(environment.COOKIE_AUTH_KEY);
  }

  public getJwt(): string {
    return this.cookieService.get(environment.COOKIE_AUTH_KEY);
  }
}
