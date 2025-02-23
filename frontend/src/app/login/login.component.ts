import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AuthService} from '../auth.service';
import {Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  loginForm!: FormGroup;

  constructor(private fb: FormBuilder, public authService: AuthService, private router: Router) {
  }

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
      isTutor: [false]
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
      error: error => console.error('Tutor login failed:', error)
    });
  }

  studentLogin(email: string, password: string) {
    this.authService.login(email, password).subscribe({
      next: response => {
        localStorage.setItem('token', response.token);
        this.router.navigate(['/']);
      },
      error: error => console.error('Student login failed:', error)
    });
  }
}
