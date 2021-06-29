import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReservationNewDialogComponent } from './reservation-new-dialog.component';

describe('ReservationNewDialogComponent', () => {
  let component: ReservationNewDialogComponent;
  let fixture: ComponentFixture<ReservationNewDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ReservationNewDialogComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReservationNewDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
