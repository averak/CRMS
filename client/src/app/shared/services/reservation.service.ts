import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { environment } from 'src/environments/environment';
import { HttpBaseService } from 'src/app/shared/services/http-base.service';
import { ReservationsModel } from 'src/app/model/reservations-model';
import { ReservationCreateRequest } from 'src/app/request/reservation-create-request';

@Injectable({
  providedIn: 'root',
})
export class ReservationService {
  constructor(private httpBaseService: HttpBaseService) {}

  getReservations(): Observable<any> {
    return this.httpBaseService.getRequest<ReservationsModel>(
      `${environment.API_PREFIX}/api/reservations`
    );
  }

  createReservation(requestBody: ReservationCreateRequest): Observable<any> {
    return this.httpBaseService.postRequest<ReservationsModel>(
      `${environment.API_PREFIX}/api/reservations`,
      requestBody
    );
  }

  deleteReservation(reservationId: number): Observable<any> {
    return this.httpBaseService.deleteRequest<ReservationsModel>(
      `${environment.API_PREFIX}/api/reservations/${reservationId}`
    );
  }
}
