import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

import { AlertService } from 'src/app/shared/services/alert.service';
import { AdmissionYearService } from 'src/app/shared/services/admission-year.service';
import { UserModel } from 'src/app/model/user-model';

@Component({
  selector: 'app-user-edit-card',
  templateUrl: './user-edit-card.component.html',
  styleUrls: ['./user-edit-card.component.css'],
})
export class UserEditCardComponent implements OnInit {
  @Input() user: UserModel = {} as UserModel;
  @Input() enableName: boolean = false;
  @Input() enableEmail: boolean = false;
  @Input() enablePassword: boolean = false;
  @Input() enableCurrentPassword: boolean = false;
  @Input() enableNewPassword: boolean = false;
  @Input() enableRoleId: boolean = false;
  @Input() enableAdmissionYear: boolean = false;

  @Input() allowNameInput: boolean = false;
  @Input() allowEmailInput: boolean = false;
  @Input() allowPasswordInput: boolean = false;
  @Input() allowCurrentPasswordInput: boolean = false;
  @Input() allowNewPasswordInput: boolean = false;
  @Input() allowRoleIdInput: boolean = false;
  @Input() allowAdmissionYearInput: boolean = false;

  @Output() submitUser: EventEmitter<UserModel> = new EventEmitter<UserModel>();
  @Output() goBackTransit: EventEmitter<any> = new EventEmitter<any>();

  hidePassword = true;
  hideCurrentPassword = true;
  hideNewPassword = true;
  admissionYears!: number[];

  constructor(
    private alertService: AlertService,
    private admissionYearService: AdmissionYearService
  ) {}

  ngOnInit(): void {
    // 入学年度リストを作成
    this.admissionYears = this.admissionYearService.getAdmissionYears();
  }

  onClickDisabledColumn(disabled: boolean): void {
    if (disabled) {
      this.alertService.openSnackBar('この項目の編集は禁止されています', 'WARN');
    }
  }

  onGoBack(): void {
    this.goBackTransit.emit();
  }

  onSubmit(): void {
    this.submitUser.emit(this.user);
  }
}
