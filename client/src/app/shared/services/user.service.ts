import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { environment } from 'src/environments/environment';
import { HttpBaseService } from 'src/app/shared/services/http-base.service';
import { UserModel } from 'src/app/model/user-model';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  constructor(private httpBaseService: HttpBaseService) {}

  getLoginUser(): Observable<UserModel> {
    return this.httpBaseService.getRequest<UserModel>(`${environment.API_PREFIX}/api/users/me`);
  }
}
