export interface UserCreateRequest {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  roleId: number;
  admissionYear: number;
}
