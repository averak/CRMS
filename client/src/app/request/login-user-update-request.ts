export interface LoginUserUpdateRequest {
  firstName: string | undefined;
  lastName: string | undefined;
  email: string | undefined;
  currentPassword: string | undefined;
  newPassword: string | undefined;
}
