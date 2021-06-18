import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
})
export class HeaderComponent implements OnInit {
  newsMessage: string = '';

  constructor() {}

  ngOnInit(): void {
    this.newsMessage = '現在のBCPレベルは3です';
  }
}
