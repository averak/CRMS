export interface UserCreateRequest {
  firstName: string | undefined;
  lastName: string | undefined;
  email: string | undefined;
  password: string | undefined;
  roleId: number | undefined;
  admissionYear: number | undefined;
}
