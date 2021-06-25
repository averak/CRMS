import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserNewFormComponent } from './user-new-form.component';

describe('UserNewFormComponent', () => {
  let component: UserNewFormComponent;
  let fixture: ComponentFixture<UserNewFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UserNewFormComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UserNewFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
