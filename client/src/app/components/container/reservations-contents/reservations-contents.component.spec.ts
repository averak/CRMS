import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReservationsContentsComponent } from './reservations-contents.component';

describe('ReservationsContentsComponent', () => {
  let component: ReservationsContentsComponent;
  let fixture: ComponentFixture<ReservationsContentsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ReservationsContentsComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReservationsContentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
