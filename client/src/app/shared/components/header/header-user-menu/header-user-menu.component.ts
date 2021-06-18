import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-header-user-menu',
  templateUrl: './header-user-menu.component.html',
  styleUrls: ['./header-user-menu.component.css'],
})
export class HeaderUserMenuComponent implements OnInit {
  userName = '阿部 健太朗';

  constructor() {}

  ngOnInit(): void {}
}
