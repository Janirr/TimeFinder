import {Component} from '@angular/core';
import {AuthService} from '../auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  email = '';
  password = '';
  isTutor = false;

  constructor(public authService: AuthService) {
  }

  login() {
    if (this.isTutor) {
      this.tutorLogin(this.email, this.password);
    } else {
      this.studentLogin(this.email, this.password);
    }
  }

  tutorLogin(email: string, password: string) {
    this.authService.tutorLogin(email, password).subscribe(
      response => {
        // Handle successful tutor login response
        localStorage.setItem('token', response.token); // Change 'token' to your token key
        // Redirect the user or perform any additional actions
      },
      error => {
        // Handle tutor login error
        console.error('Tutor login failed:', error);
        // Display error message or perform any additional actions
      }
    );
  }

  studentLogin(email: string, password: string) {
    this.authService.login(email, password).subscribe(
      response => {
        // Handle successful login response
        localStorage.setItem('token', response.token); // Change 'token' to your token key
        // Redirect the user or perform any additional actions
      },
      error => {
        // Handle login error
        console.error('Student login failed:', error);
        // Display error message or perform any additional actions
      }
    );
  }

  register() {
    // Implement the registration logic
  }

  logout() {
    this.authService.logout();
  }
}
