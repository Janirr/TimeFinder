import {Component, OnInit} from '@angular/core';
import {NavigationEnd, Router} from '@angular/router';
import {AuthService} from './auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  providers: [AuthService]  // AuthService provided here
})
export class AppComponent implements OnInit {
  title = 'Korepetycje';
  isRegisterPage: boolean = false;  // Flag to check if we're on the register page

  constructor(public authService: AuthService, private router: Router) {
  }

  ngOnInit(): void {
    // Subscribe to router events to track the current route
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.isRegisterPage = this.router.url === '/register';  // Check if we're on the '/register' route
      }
    });
  }
}
