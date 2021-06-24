import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';

import { UserModel } from 'src/app/model/user-model';
import { UserService } from 'src/app/shared/services/user.service';
import { AlertService } from 'src/app/shared/services/alert.service';

@Injectable({
  providedIn: 'root',
})
export class AdminGuard implements CanActivate {
  constructor(private userService: UserService, private alertService: AlertService) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    let result: boolean = false;

    const loginUser = this.userService.loginUser;

    if (loginUser.roleId == 1) {
      return true;
    } else {
      this.alertService.openSnackBar('このページへのアクセスは許可されていません', 'WARN');
      return false;
    }
  }
}
