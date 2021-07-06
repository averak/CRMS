import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { environment } from 'src/environments/environment';
import { HttpBaseService } from 'src/app/shared/services/http-base.service';
import { ReservationCreateRequest } from 'src/app/request/reservation-create-request';
import { ReservationUpdateRequest } from 'src/app/request/reservation-update-request';

@Injectable({
  providedIn: 'root',
})
export class ReservationService {
  constructor(private httpBaseService: HttpBaseService) {}

  getReservations(): Observable<any> {
    return this.httpBaseService.getRequest<any>(`${environment.API_PREFIX}/api/reservations`);
  }

  createReservation(requestBody: ReservationCreateRequest): Observable<any> {
    return this.httpBaseService.postRequest<any>(
      `${environment.API_PREFIX}/api/reservations`,
      requestBody
    );
  }

  updateReservation(reservationId: number, requestBody: ReservationUpdateRequest): Observable<any> {
    return this.httpBaseService.putRequest<any>(
      `${environment.API_PREFIX}/api/reservations/${reservationId}`,
      requestBody
    );
  }

  deleteReservation(reservationId: number): Observable<any> {
    return this.httpBaseService.deleteRequest<any>(
      `${environment.API_PREFIX}/api/reservations/${reservationId}`
    );
  }
}
