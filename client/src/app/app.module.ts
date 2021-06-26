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
import { MypageComponent } from './components/page/mypage/mypage.component';
import { UserEditFormComponent } from './components/container/user-edit-form/user-edit-form.component';
import { UserNewFormComponent } from './components/container/user-new-form/user-new-form.component';
import { MypageEditFormComponent } from './components/container/mypage-edit-form/mypage-edit-form.component';
import { UserEditCardComponent } from './components/presentational/user-edit-card/user-edit-card.component';
import { LoginCardComponent } from './components/presentational/login-card/login-card.component';
import { ProfileEditComponent } from './components/page/mypage/profile-edit/profile-edit.component';
import { PasswordEditComponent } from './components/page/mypage/password-edit/password-edit.component';
import { UsersComponent } from './components/page/admin/users/users.component';
import { UsersEditComponent } from './components/page/admin/users-edit/users-edit.component';
import { UsersNewComponent } from './components/page/admin/users-new/users-new.component';

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
    MypageComponent,
    UserEditFormComponent,
    UserNewFormComponent,
    MypageEditFormComponent,
    UserEditCardComponent,
    LoginCardComponent,
    ProfileEditComponent,
    PasswordEditComponent,
    UsersComponent,
    UsersEditComponent,
    UsersNewComponent,
  ],
  imports: [BrowserModule, SharedModule, AppRoutingModule],
  providers: [CookieService],
  bootstrap: [AppComponent],
})
export class AppModule {}
