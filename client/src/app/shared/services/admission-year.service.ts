import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class AdmissionYearService {
  constructor() {}

  getAdmissionYears(): number[] {
    const currentYear: number = new Date().getFullYear();
    return [...Array(8)].map((_: undefined, idx: number) => idx + currentYear - 7);
  }
}
