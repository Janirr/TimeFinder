import {Component, OnInit} from '@angular/core';
import {AbstractControl, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AuthService} from '../auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  registerForm!: FormGroup;

  constructor(private fb: FormBuilder, public authService: AuthService) {
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
    const {registerEmail, registerPassword, name, surname, phoneNumber, isTutor} = this.registerForm.value;
    this.authService.register(registerEmail, registerPassword, name, surname, phoneNumber, isTutor)
      .subscribe({
        next: () => console.info('Registration successful'),
        error: error => console.error('Registration failed:', error),
        complete: () => console.info('Registration completed')
      });
  }
}
