import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-user-create-button',
  templateUrl: './user-create-button.component.html',
  styleUrls: ['./user-create-button.component.css'],
})
export class UserCreateButtonComponent implements OnInit {
  constructor(private router: Router) {}

  ngOnInit(): void {}

  onClick(): void {
    this.router.navigate(['/admin/users/new']);
  }
}
