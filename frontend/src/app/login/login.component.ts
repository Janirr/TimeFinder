import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {SnackBarService} from '../snack-bar.service'; // Import the SnackBarService
import {AuthService} from '../auth.service'; // Import the AuthService
import {Router} from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  standalone: false
})
export class LoginComponent implements OnInit {
  loginForm!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private snackBarService: SnackBarService // Inject SnackBarService
  ) {
  }

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
      isTutor: [false],
    });
  }

  login() {
    if (this.loginForm.invalid) return;

    const {email, password, isTutor} = this.loginForm.value;
    isTutor ? this.tutorLogin(email, password) : this.studentLogin(email, password);
  }

  tutorLogin(email: string, password: string) {
    this.authService.tutorLogin(email, password).subscribe({
      next: response => {
        localStorage.setItem('token', response.token);
        this.router.navigate(['/']);
      },
      error: error => {
        console.error('Tutor login failed:', error);
        this.snackBarService.showError(error);
      }
    });
  }

  studentLogin(email: string, password: string) {
    this.authService.login(email, password).subscribe({
      next: response => {
        localStorage.setItem('token', response.token);
        this.router.navigate(['/']);
      },
      error: error => {
        console.error('Student login failed:', error);
        this.snackBarService.showError(error);
      }
    });
  }
}
