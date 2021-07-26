import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ActivitiesContentsComponent } from './activities-contents.component';

describe('ActivitiesContentsComponent', () => {
  let component: ActivitiesContentsComponent;
  let fixture: ComponentFixture<ActivitiesContentsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ActivitiesContentsComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ActivitiesContentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
