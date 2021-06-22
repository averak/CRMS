import { TestBed } from '@angular/core/testing';
import { AppComponent } from './app.component';

// shared module
import { SharedModule } from './shared/shared.module';

import { LoginComponent } from './components/page/login/login.component';

describe('AppComponent', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AppComponent, LoginComponent],
      imports: [SharedModule],
    }).compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it(`should have as title 'CRMS'`, () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app.title).toEqual('CRMS');
  });
});
