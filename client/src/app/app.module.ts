import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { CookieService } from 'ngx-cookie-service';

// shared module
import { SharedModule } from './shared/shared.module';

// angular calendar
import { FlatpickrModule } from 'angularx-flatpickr';
import { CalendarModule, DateAdapter } from 'angular-calendar';
import { adapterFactory } from 'angular-calendar/date-adapters/date-fns';

// components
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './components/page/login/login.component';
import { LoginFormComponent } from './components/container/login-form/login-form.component';
import { ErrorPageComponent } from './components/page/error-page/error-page.component';
import { ReservationsComponent } from './components/page/reservations/reservations.component';
import { ReservationsCalendarComponent } from './components/container/reservations-calendar/reservations-calendar.component';
import { SidenavComponent } from './components/presentational/sidenav/sidenav.component';
import { TitleBoxComponent } from './components/presentational/title-box/title-box.component';
import { DashboardComponent } from './components/page/dashboard/dashboard.component';
import { UsersTableComponent } from './components/container/users-table/users-table.component';
import { AdminComponent } from './components/page/admin/admin.component';
import { MypageComponent } from './components/page/mypage/mypage.component';
import { UserEditFormComponent } from './components/container/user-edit-form/user-edit-form.component';
import { UserNewFormComponent } from './components/container/user-new-form/user-new-form.component';
import { UserEditCardComponent } from './components/presentational/user-edit-card/user-edit-card.component';
import { LoginCardComponent } from './components/presentational/login-card/login-card.component';
import { ProfileEditComponent } from './components/page/mypage/profile-edit/profile-edit.component';
import { PasswordEditComponent } from './components/page/mypage/password-edit/password-edit.component';
import { UsersComponent } from './components/page/admin/users/users.component';
import { UsersEditComponent } from './components/page/admin/users-edit/users-edit.component';
import { UsersNewComponent } from './components/page/admin/users-new/users-new.component';
import { MypageProfileEditFormComponent } from './components/container/mypage-profile-edit-form/mypage-profile-edit-form.component';
import { MypagePasswordEditFormComponent } from './components/container/mypage-password-edit-form/mypage-password-edit-form.component';
import { UsersContentsComponent } from './components/container/users-contents/users-contents.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    LoginFormComponent,
    ErrorPageComponent,
    ReservationsComponent,
    ReservationsCalendarComponent,
    SidenavComponent,
    TitleBoxComponent,
    DashboardComponent,
    UsersTableComponent,
    AdminComponent,
    MypageComponent,
    UserEditFormComponent,
    UserNewFormComponent,
    UserEditCardComponent,
    LoginCardComponent,
    ProfileEditComponent,
    PasswordEditComponent,
    UsersComponent,
    UsersEditComponent,
    UsersNewComponent,
    MypageProfileEditFormComponent,
    MypagePasswordEditFormComponent,
    UsersContentsComponent,
  ],
  imports: [
    BrowserModule,
    SharedModule,
    AppRoutingModule,
    FlatpickrModule.forRoot(),
    CalendarModule.forRoot({ provide: DateAdapter, useFactory: adapterFactory }),
  ],
  providers: [CookieService],
  bootstrap: [AppComponent],
})
export class AppModule {}
