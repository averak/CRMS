import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

// page components
import { LoginComponent } from './components/page/login/login.component';
import { DashboardComponent } from './components/page/dashboard/dashboard.component';
import { ReservationsComponent } from './components/page/reservations/reservations.component';
import { AdminComponent } from './components/page/admin/admin.component';
import { AdminUsersComponent } from './components/page/admin/admin-users/admin-users.component';
import { AdminUsersNewComponent } from './components/page/admin/admin-users-new/admin-users-new.component';
import { AdminUsersEditComponent } from './components/page/admin/admin-users-edit/admin-users-edit.component';
import { MypageComponent } from './components/page/mypage/mypage.component';
import { MypageEditComponent } from './components/page/mypage/mypage-edit/mypage-edit.component';
import { ErrorPageComponent } from './components/page/error-page/error-page.component';
import { SidenavComponent } from './components/container/sidenav/sidenav.component';
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
      {
        path: '',
        component: SidenavComponent,
        children: [
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
              { path: 'users', component: AdminUsersComponent },
              {
                path: 'users/new',
                component: AdminUsersNewComponent,
              },
              {
                path: 'users/:userId/edit',
                component: AdminUsersEditComponent,
              },
            ],
          },
          {
            path: 'mypage',
            component: MypageComponent,
            children: [
              {
                path: 'edit',
                component: MypageEditComponent,
              },
            ],
          },
        ],
      },
    ],
  },
  { path: 'error', component: ErrorPageComponent },
  { path: '', component: LoginComponent },
  { path: '**', redirectTo: '/error?status_code=404', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {})],
  exports: [RouterModule],
})
export class AppRoutingModule {}
