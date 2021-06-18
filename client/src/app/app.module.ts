import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

// shared module
import { SharedModule } from './shared/shared.module';

// components
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './components/page/login/login.component';
import { LoginFormComponent } from './components/container/login-form/login-form.component';
import { LoginCardComponent } from './components/presentational/login-card/login-card.component';
import { ErrorPageComponent } from './components/page/error-page/error-page.component';
import { ReservationsComponent } from './components/page/reservations/reservations.component';
import { SidenavComponent } from './components/container/sidenav/sidenav.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    LoginFormComponent,
    LoginCardComponent,
    ErrorPageComponent,
    ReservationsComponent,
    SidenavComponent,
  ],
  imports: [BrowserModule, AppRoutingModule, SharedModule],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
