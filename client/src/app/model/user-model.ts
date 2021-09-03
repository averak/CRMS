export interface UserModel {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  currentPassword: string;
  newPassword: string;
  roleId: number;
  admissionYear: number;
}

export interface UsersModel {
  users: UserModel[];
}

export interface AccessTokenModel {
  accessToken: string;
  tokenType: string;
}
