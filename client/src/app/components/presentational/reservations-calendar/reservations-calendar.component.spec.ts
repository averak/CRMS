import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReservationsCalendarComponent } from './reservations-calendar.component';

describe('ReservationsCalendarComponent', () => {
  let component: ReservationsCalendarComponent;
  let fixture: ComponentFixture<ReservationsCalendarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ReservationsCalendarComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReservationsCalendarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
