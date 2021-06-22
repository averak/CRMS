import { TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';

// shared module
import { SharedModule } from 'src/app/shared/shared.module';

import { AuthGuard } from './auth.guard';

describe('AuthGuard', () => {
  let guard: AuthGuard;

  beforeEach(() => {
    TestBed.configureTestingModule({ imports: [RouterTestingModule, SharedModule] });
    guard = TestBed.inject(AuthGuard);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
