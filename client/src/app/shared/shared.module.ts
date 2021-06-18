import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

// UI modules
import { MatInputModule } from '@angular/material/input';
import { MatCardModule } from '@angular/material/card';
import { MatMenuModule } from '@angular/material/menu';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatTableModule } from '@angular/material/table';
import { MatDividerModule } from '@angular/material/divider';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

// components
import { FooterComponent } from './components/footer/footer.component';
import { SnackBarComponent } from './components/snack-bar/snack-bar.component';

@NgModule({
  declarations: [FooterComponent, SnackBarComponent],
  imports: [
    FormsModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    MatInputModule,
    MatCardModule,
    MatMenuModule,
    MatIconModule,
    MatButtonModule,
    MatTableModule,
    MatDividerModule,
    MatSlideToggleModule,
    MatSelectModule,
    MatSnackBarModule,
    MatSidenavModule,
    MatListModule,
    MatProgressSpinnerModule,
  ],
  exports: [
    FormsModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    MatInputModule,
    MatCardModule,
    MatMenuModule,
    MatIconModule,
    MatButtonModule,
    MatTableModule,
    MatDividerModule,
    MatSlideToggleModule,
    MatSelectModule,
    MatSnackBarModule,
    MatSidenavModule,
    MatListModule,
    MatProgressSpinnerModule,

    // components
    FooterComponent,
    SnackBarComponent,
  ],
})
export class SharedModule {}
