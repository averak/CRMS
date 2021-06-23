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
  ],
  imports: [BrowserModule, AppRoutingModule, SharedModule],
  providers: [CookieService],
  bootstrap: [AppComponent],
})
export class AppModule {}
