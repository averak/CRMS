import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminUsersNewComponent } from './admin-users-new.component';

describe('AdminUsersNewComponent', () => {
  let component: AdminUsersNewComponent;
  let fixture: ComponentFixture<AdminUsersNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AdminUsersNewComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminUsersNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
