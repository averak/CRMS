import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatSnackBarRef, MAT_SNACK_BAR_DATA } from '@angular/material/snack-bar';

// shared module
import { SharedModule } from 'src/app/shared/shared.module';

import { SnackBarComponent } from './snack-bar.component';

describe('SnackBarComponent', () => {
  let component: SnackBarComponent;
  let fixture: ComponentFixture<SnackBarComponent>;

  let data = {
    message: 'SAMPLE',
    level: 'SUCCESS',
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SnackBarComponent],
      imports: [SharedModule],
      providers: [
        { provide: MAT_SNACK_BAR_DATA, useValue: null },
        { provide: MatSnackBarRef, useValue: {} },
      ],
    }).compileComponents();
  });

  beforeEach(() => {
    TestBed.overrideProvider(MAT_SNACK_BAR_DATA, { useValue: data });
    fixture = TestBed.createComponent(SnackBarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
