import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientModule } from '@angular/common/http';

// UI modules
import { MatInputModule } from '@angular/material/input';
import { MatCardModule } from '@angular/material/card';
import { MatMenuModule } from '@angular/material/menu';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatTableModule } from '@angular/material/table';
import { MatDividerModule } from '@angular/material/divider';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatDialogModule } from '@angular/material/dialog';
import { ScrollingModule } from '@angular/cdk/scrolling';
import { FlexLayoutModule } from '@angular/flex-layout';
import { NgbModalModule } from '@ng-bootstrap/ng-bootstrap';
import { NgApexchartsModule } from 'ng-apexcharts';

// components
import { ConfirmDialogComponent } from 'src/app/shared/components/confirm-dialog/confirm-dialog.component';
import { SnackBarComponent } from 'src/app/shared/components/snack-bar/snack-bar.component';
import { HeaderComponent } from 'src/app/shared/components/header/header.component';
import { SidenavComponent } from 'src/app/shared/components/sidenav/sidenav.component';
import { HeaderUserMenuComponent } from 'src/app/shared/components/header/header-user-menu/header-user-menu.component';
import { FooterComponent } from 'src/app/shared/components/footer/footer.component';

// directives
import { PreventDoubleClickDirective } from './directives/prevent-double-click.directive';

@NgModule({
  declarations: [
    ConfirmDialogComponent,
    SnackBarComponent,
    HeaderComponent,
    SidenavComponent,
    HeaderUserMenuComponent,
    FooterComponent,
    PreventDoubleClickDirective,
  ],
  imports: [
    FormsModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    RouterTestingModule,
    HttpClientModule,
    MatInputModule,
    MatCardModule,
    MatMenuModule,
    MatIconModule,
    MatToolbarModule,
    MatButtonModule,
    MatTableModule,
    MatDividerModule,
    MatSlideToggleModule,
    MatSelectModule,
    MatSnackBarModule,
    MatSidenavModule,
    MatListModule,
    MatProgressSpinnerModule,
    MatGridListModule,
    MatPaginatorModule,
    MatDialogModule,
    ScrollingModule,
    FlexLayoutModule,
    NgbModalModule,
    NgApexchartsModule,
  ],
  exports: [
    FormsModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    RouterTestingModule,
    MatInputModule,
    MatCardModule,
    MatMenuModule,
    MatIconModule,
    MatToolbarModule,
    MatButtonModule,
    MatTableModule,
    MatDividerModule,
    MatSlideToggleModule,
    MatSelectModule,
    MatSnackBarModule,
    MatSidenavModule,
    MatListModule,
    MatProgressSpinnerModule,
    MatGridListModule,
    MatPaginatorModule,
    MatDialogModule,
    ScrollingModule,
    FlexLayoutModule,
    NgbModalModule,
    NgApexchartsModule,

    // components
    ConfirmDialogComponent,
    SnackBarComponent,
    HeaderComponent,
    FooterComponent,

    // directives
    PreventDoubleClickDirective,
  ],
})
export class SharedModule {}
