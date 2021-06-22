import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { AuthService } from 'src/app/shared/services/auth.service';
import { AlertService } from 'src/app/shared/services/alert.service';

@Component({
  selector: 'app-header-user-menu',
  templateUrl: './header-user-menu.component.html',
  styleUrls: ['./header-user-menu.component.css'],
})
export class HeaderUserMenuComponent implements OnInit {
  userName = '阿部 健太朗';

  constructor(
    private authService: AuthService,
    private alertService: AlertService,
    private router: Router
  ) {}

  ngOnInit(): void {}

  onProfileClick(): void {
    // FIXME: プロフィール表示ページへ遷移
  }

  onLogoutClick(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
    this.alertService.openSnackBar('ログアウトしました', 'SUCCESS');
  }
}
