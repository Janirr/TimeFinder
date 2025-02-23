import {Component, OnInit} from '@angular/core';
import {AbstractControl, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AuthService} from '../auth.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  registerForm!: FormGroup;

  constructor(private fb: FormBuilder, private authService: AuthService, private router: Router) {
  }

  ngOnInit(): void {
    this.registerForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', [Validators.required]],
      name: ['', Validators.required],
      surname: ['', Validators.required],
      phoneNumber: ['', [Validators.required, Validators.pattern('^[0-9]{9,}$')]],
      isTutor: [false]
    }, {validators: this.passwordsMatchValidator});
  }

  passwordsMatchValidator(form: AbstractControl) {
    const password = form.get('password')?.value;
    const confirmPassword = form.get('confirmPassword')?.value;
    return password === confirmPassword ? null : {passwordsMismatch: true};
  }

  register() {
    if (this.registerForm.invalid) return;

    const {email, password, name, surname, phoneNumber, isTutor} = this.registerForm.value;
    this.authService.register(email, password, name, surname, phoneNumber, isTutor)
      .subscribe({
        next: async () => { // Mark the function as async
          console.info('Registration successful');
          try {
            await this.router.navigate(['/login']); // Properly handle the navigation promise
            console.info('Navigation to login successful');
          } catch (error) {
            console.error('Navigation to login failed:', error);
          }
        },
      });
  }
}
