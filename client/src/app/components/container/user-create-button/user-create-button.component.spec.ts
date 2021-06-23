import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserCreateButtonComponent } from './user-create-button.component';

describe('UserCreateButtonComponent', () => {
  let component: UserCreateButtonComponent;
  let fixture: ComponentFixture<UserCreateButtonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UserCreateButtonComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UserCreateButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
