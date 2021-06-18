import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-header-news',
  templateUrl: './header-news.component.html',
  styleUrls: ['./header-news.component.css'],
})
export class HeaderNewsComponent implements OnInit {
  message: string = '';

  constructor() {}

  ngOnInit(): void {
    this.message = '現在のBCPレベルは3です。感染対策を徹底してください。';
  }
}
