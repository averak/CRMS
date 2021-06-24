import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { UserModel } from 'src/app/model/user-model';
import { UserService } from 'src/app/shared/services/user.service';
import { AuthService } from 'src/app/shared/services/auth.service';
import { AlertService } from 'src/app/shared/services/alert.service';

@Component({
  selector: 'app-header-user-menu',
  templateUrl: './header-user-menu.component.html',
  styleUrls: ['./header-user-menu.component.css'],
})
export class HeaderUserMenuComponent implements OnInit {
  userName = '';
  user!: UserModel;

  constructor(
    private userService: UserService,
    private authService: AuthService,
    private alertService: AlertService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.userService.getLoginUser().subscribe(
      (user: UserModel) => {
        this.user = user;
        this.userService.setLoginUser(this.user);
        this.userName = `${user.lastName} ${user.firstName}`;
      },
      (error) => {
        this.alertService.openSnackBar(error, 'ERROR');
      }
    );
  }

  onProfileClick(): void {
    // FIXME: プロフィール表示ページへ遷移
  }

  onLogoutClick(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
    this.alertService.openSnackBar('ログアウトしました', 'SUCCESS');
  }
}
