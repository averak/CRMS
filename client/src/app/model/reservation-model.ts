import { UserModel } from './user-model';

export interface ReservationModel {
  id: number;
  startAt: Date;
  finishAt: Date;
  user: UserModel;
}
