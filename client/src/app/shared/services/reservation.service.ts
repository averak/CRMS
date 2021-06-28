import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { ReservationModel } from 'src/app/model/reservation-model';
import { ReservationsModel } from 'src/app/model/reservations-model';

@Injectable({
  providedIn: 'root',
})
export class ReservationService {
  constructor() {}
}
