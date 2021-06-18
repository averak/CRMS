import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

// page components
import { LoginComponent } from './components/page/login/login.component';
import { ReservationsComponent } from './components/page/reservations/reservations.component';
import { ErrorPageComponent } from './components/page/error-page/error-page.component';

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'reservations', component: ReservationsComponent },
  { path: 'error', component: ErrorPageComponent },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: '**', redirectTo: '/error?status_code=404', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
