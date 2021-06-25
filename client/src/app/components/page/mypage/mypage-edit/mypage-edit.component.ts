import { Component, OnInit, HostListener } from '@angular/core';

import { OnBeforeUnload } from 'src/app/shared/guards/before-unload.guard';

@Component({
  selector: 'app-mypage-edit',
  templateUrl: './mypage-edit.component.html',
  styleUrls: ['./mypage-edit.component.css'],
})
export class MypageEditComponent implements OnInit, OnBeforeUnload {
  shouldConfirmOnBeforeunload = true;

  constructor() {}

  ngOnInit(): void {}

  @HostListener('window:beforeunload', ['$event'])
  beforeUnload(e: Event) {
    if (this.shouldConfirmOnBeforeunload) {
      e.returnValue = true;
    }
  }
}
