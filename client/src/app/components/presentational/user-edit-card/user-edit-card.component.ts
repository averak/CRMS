import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

import { AdmissionYearService } from 'src/app/shared/services/admission-year.service';
import { UserModel } from 'src/app/model/user-model';

@Component({
  selector: 'app-user-edit-card',
  templateUrl: './user-edit-card.component.html',
  styleUrls: ['./user-edit-card.component.css'],
})
export class UserEditCardComponent implements OnInit {
  @Input() user: UserModel = {} as UserModel;
  @Input() enablePassword: boolean = true;
  @Output() submitUser: EventEmitter<UserModel> = new EventEmitter<UserModel>();
  @Output() goBackTransit: EventEmitter<any> = new EventEmitter<any>();

  hide = true;
  admissionYears!: number[];

  constructor(private admissionYearService: AdmissionYearService) {}

  ngOnInit(): void {
    console.log(this.user);
    // 入学年度リストを作成
    this.admissionYears = this.admissionYearService.getAdmissionYears();
  }

  onGoBack(): void {
    this.goBackTransit.emit();
  }

  onSubmit(): void {
    this.submitUser.emit(this.user);
  }
}
