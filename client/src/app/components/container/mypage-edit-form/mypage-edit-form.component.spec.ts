import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MypageEditFormComponent } from './mypage-edit-form.component';

describe('MypageEditFormComponent', () => {
  let component: MypageEditFormComponent;
  let fixture: ComponentFixture<MypageEditFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MypageEditFormComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MypageEditFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
