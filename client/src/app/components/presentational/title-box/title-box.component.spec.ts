import { ComponentFixture, TestBed } from '@angular/core/testing';

// shared module
import { SharedModule } from 'src/app/shared/shared.module';

import { TitleBoxComponent } from './title-box.component';

describe('TitleBoxComponent', () => {
  let component: TitleBoxComponent;
  let fixture: ComponentFixture<TitleBoxComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [TitleBoxComponent],
      imports: [SharedModule],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TitleBoxComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
