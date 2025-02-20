import {Component, OnInit} from '@angular/core';
import {NavigationEnd, Router} from '@angular/router';
import {AuthService} from './auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  providers: [AuthService]
})
export class AppComponent implements OnInit {
  title = 'Korepetycje';
  isRegisterPage: boolean = false;
  isNotFoundPage: boolean = false;  // Flag to check if we're on the 404 page

  constructor(public authService: AuthService, private router: Router) {
  }

  ngOnInit(): void {
    // Subscribe to router events to track the current route
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        // Check if we're on the '/register' route
        this.isRegisterPage = this.router.url === '/register';

        // Check if we're on the 404 page (based on the URL)
        this.isNotFoundPage = this.router.url === '/404';  // Adjust the URL if necessary for your 404 route
      }
    });
  }
}
