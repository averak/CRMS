import { TestBed } from '@angular/core/testing';

import { AdmissionYearService } from './admission-year.service';

describe('AdmissionYearService', () => {
  let service: AdmissionYearService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AdmissionYearService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
