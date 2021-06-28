import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReservationNewFormComponent } from './reservation-new-form.component';

describe('ReservationNewFormComponent', () => {
  let component: ReservationNewFormComponent;
  let fixture: ComponentFixture<ReservationNewFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ReservationNewFormComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReservationNewFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
