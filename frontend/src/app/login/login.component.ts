import { Component } from '@angular/core';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  username = '';
  password = '';

  constructor(public authService: AuthService) {}

  login() {
    this.authService.login(this.username, this.password).subscribe(
      response => {
        // Handle successful login response
        localStorage.setItem('token', response.token); // Change 'token' to your token key
        // Redirect the user or perform any additional actions
      },
      error => {
        // Handle login error
        console.error('Login failed:', error);
        // Display error message or perform any additional actions
      }
    );
  }
  register(){

  }
  logout() {
    this.authService.logout();
  }
}
