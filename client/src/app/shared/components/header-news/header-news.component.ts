import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-header-news',
  templateUrl: './header-news.component.html',
  styleUrls: ['./header-news.component.css'],
})
export class HeaderNewsComponent implements OnInit {
  @Input() message: string = '';

  constructor() {}

  ngOnInit(): void {}
}
