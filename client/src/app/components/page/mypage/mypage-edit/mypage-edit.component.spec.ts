import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MypageEditComponent } from './mypage-edit.component';

describe('MypageEditComponent', () => {
  let component: MypageEditComponent;
  let fixture: ComponentFixture<MypageEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MypageEditComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MypageEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
