import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReservationNewCardComponent } from './reservation-new-card.component';

describe('ReservationNewCardComponent', () => {
  let component: ReservationNewCardComponent;
  let fixture: ComponentFixture<ReservationNewCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ReservationNewCardComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReservationNewCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
