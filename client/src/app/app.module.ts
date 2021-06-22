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
import { AdminComponent } from './components/page/admin/admin.component';
import { DashboardComponent } from './components/page/dashboard/dashboard.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    LoginFormComponent,
    ErrorPageComponent,
    ReservationsComponent,
    SidenavComponent,
    TitleBoxComponent,
    AdminComponent,
    DashboardComponent,
  ],
  imports: [BrowserModule, AppRoutingModule, SharedModule],
  providers: [CookieService],
  bootstrap: [AppComponent],
})
export class AppModule {}
