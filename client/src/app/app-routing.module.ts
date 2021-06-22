import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

// page components
import { LoginComponent } from './components/page/login/login.component';
import { DashboardComponent } from './components/page/dashboard/dashboard.component';
import { ReservationsComponent } from './components/page/reservations/reservations.component';
import { AdminComponent } from './components/page/admin/admin.component';
import { ErrorPageComponent } from './components/page/error-page/error-page.component';

// guards
import { AuthGuard } from './shared/guards/auth.guard';
import { LoginedGuard } from './shared/guards/logined.guard';

const routes: Routes = [
  { path: 'login', component: LoginComponent, canActivate: [LoginedGuard] },
  { path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard] },
  { path: 'reservations', component: ReservationsComponent, canActivate: [AuthGuard] },
  { path: 'admin', component: AdminComponent, canActivate: [AuthGuard] },
  { path: 'error', component: ErrorPageComponent },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: '**', redirectTo: '/error?status_code=404', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
