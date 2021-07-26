import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

// page components
// login
import { LoginComponent } from './components/page/login/login.component';
// dashboard
import { DashboardComponent } from './components/page/dashboard/dashboard.component';
// reservations
import { ReservationsComponent } from './components/page/reservations/reservations.component';
// activity
import { ActivitiesComponent } from './components/page/activities/activities.component';
// admin
import { AdminComponent } from './components/page/admin/admin.component';
import { UsersComponent } from './components/page/admin/users/users.component';
import { UsersNewComponent } from './components/page/admin/users-new/users-new.component';
import { UsersEditComponent } from './components/page/admin/users-edit/users-edit.component';
// mypage
import { MypageComponent } from './components/page/mypage/mypage.component';
import { ProfileEditComponent } from './components/page/mypage/profile-edit/profile-edit.component';
import { PasswordEditComponent } from './components/page/mypage/password-edit/password-edit.component';
// others
import { ErrorPageComponent } from './components/page/error-page/error-page.component';
import { HeaderComponent } from './shared/components/header/header.component';

// guards
import { AuthGuard } from './shared/guards/auth.guard';
import { LoginedGuard } from './shared/guards/logined.guard';
import { AdminGuard } from './shared/guards/admin.guard';
// import { BeforeUnloadGuard } from './shared/guards/before-unload.guard';

const routes: Routes = [
  { path: 'login', component: LoginComponent, canActivate: [LoginedGuard] },
  {
    path: '',
    component: HeaderComponent,
    canActivate: [AuthGuard],
    children: [
      { path: '', component: DashboardComponent },
      {
        path: 'dashboard',
        component: DashboardComponent,
      },
      { path: 'reservations', component: ReservationsComponent },
      {
        path: 'admin',
        component: AdminComponent,
        canActivate: [AdminGuard],
        children: [
          { path: 'users', component: UsersComponent },
          {
            path: 'users/new',
            component: UsersNewComponent,
          },
          {
            path: 'users/:userId/edit',
            component: UsersEditComponent,
          },
        ],
      },
      { path: 'activities', component: ActivitiesComponent },
      {
        path: 'mypage',
        component: MypageComponent,
        children: [
          {
            path: 'profile',
            component: ProfileEditComponent,
          },
          {
            path: 'password',
            component: PasswordEditComponent,
          },
        ],
      },
    ],
  },
  { path: 'error', component: ErrorPageComponent },
  { path: '**', redirectTo: '/error?status_code=404', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {})],
  exports: [RouterModule],
})
export class AppRoutingModule {}
