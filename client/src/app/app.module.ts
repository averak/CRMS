import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { CookieService } from 'ngx-cookie-service';

// shared module
import { SharedModule } from './shared/shared.module';

// components
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './components/page/login/login.component';
import { LoginFormComponent } from './components/container/login-form/login-form.component';
import { ErrorPageComponent } from './components/page/error-page/error-page.component';
import { ReservationsComponent } from './components/page/reservations/reservations.component';
import { SidenavComponent } from './components/container/sidenav/sidenav.component';
import { TitleBoxComponent } from './components/container/title-box/title-box.component';
import { DashboardComponent } from './components/page/dashboard/dashboard.component';
import { UsersTableComponent } from './components/container/users-table/users-table.component';
import { UserCreateButtonComponent } from './components/container/user-create-button/user-create-button.component';
import { AdminComponent } from './components/page/admin/admin.component';
import { AdminUsersComponent } from './components/page/admin/admin-users/admin-users.component';
import { AdminUsersNewComponent } from './components/page/admin/admin-users-new/admin-users-new.component';
import { AdminUsersEditComponent } from './components/page/admin/admin-users-edit/admin-users-edit.component';
import { MypageComponent } from './components/page/mypage/mypage.component';
import { MypageEditComponent } from './components/page/mypage/mypage-edit/mypage-edit.component';
import { UserEditFormComponent } from './components/container/user-edit-form/user-edit-form.component';
import { UserNewFormComponent } from './components/container/user-new-form/user-new-form.component';
import { MypageEditFormComponent } from './components/container/mypage-edit-form/mypage-edit-form.component';
import { UserEditCardComponent } from './components/presentational/user-edit-card/user-edit-card.component';
import { LoginCardComponent } from './components/presentational/login-card/login-card.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    LoginFormComponent,
    ErrorPageComponent,
    ReservationsComponent,
    SidenavComponent,
    TitleBoxComponent,
    DashboardComponent,
    UsersTableComponent,
    UserCreateButtonComponent,
    AdminComponent,
    AdminUsersComponent,
    AdminUsersNewComponent,
    AdminUsersEditComponent,
    MypageComponent,
    MypageEditComponent,
    UserEditFormComponent,
    UserNewFormComponent,
    MypageEditFormComponent,
    UserEditCardComponent,
    LoginCardComponent,
  ],
  imports: [BrowserModule, SharedModule, AppRoutingModule],
  providers: [CookieService],
  bootstrap: [AppComponent],
})
export class AppModule {}
