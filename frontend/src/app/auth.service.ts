import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpHeaders} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {catchError, tap} from 'rxjs/operators';
import {UserService} from './user.service';
import * as CryptoJS from 'crypto-js';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  username = 'janir';
  password = 'root';
  credentials = btoa(this.username + ':' + this.password); // Encode the username and password
  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': 'Basic ' + this.credentials // Add the Basic Authentication header
    })
  };
  private Url = 'http://localhost:8080/login'; // Update with your API URL
  private studentLoggedIn: boolean = false;
  private tutorLoggedIn: boolean = false;

  constructor(private http: HttpClient, private userService: UserService) {
  }

  login(email: string, password: string): Observable<any> {
    const hashedPassword = this.hashPassword(password);  // Hash the password
    const credentials = {
      email,
      password: hashedPassword
    };
    return this.http.post(this.Url + '/student', credentials, {
      ...this.httpOptions,
      responseType: 'text' // Set the response type to 'text'
    })
      .pipe(
        tap((response: any) => {
          if (response !== null && response !== 'UNAUTHORIZED') {
            const student = JSON.parse(response);
            console.log(student);
            this.studentLoggedIn = true;
            this.userService.student = student; // Assign the response string directly to email
          } else {
            this.studentLoggedIn = false;
          }
        }),
        catchError((error: HttpErrorResponse) => {
          console.error('Login failed', error);
          return throwError('Login failed'); // Use throwError to create an error observable
        })
      );
  }

  tutorLogin(email: string, password: string): Observable<any> {
    const hashedPassword = this.hashPassword(password);  // Hash the password
    const credentials = {
      email,
      password: hashedPassword
    };
    return this.http.post(this.Url + '/tutor', credentials, {
      ...this.httpOptions,
      responseType: 'text' // Set the response type to 'text'
    })
      .pipe(
        tap((response: any) => {
          if (response !== null && response !== 'UNAUTHORIZED') {
            const tutor = JSON.parse(response);
            console.log(tutor);
            this.tutorLoggedIn = true;
            this.userService.tutor = tutor; // Assign the response string directly to email
          } else {
            this.tutorLoggedIn = false;
          }
        }),
        catchError((error: HttpErrorResponse) => {
          console.error('Login failed', error);
          return throwError(() => 'Login failed'); // Use throwError to create an error observable
        })
      );
  }

  register(email: string, password: string, name: string, surname: string, phoneNumber: number, isTutor: boolean): Observable<any> {
    const hashedPassword = this.hashPassword(password);  // Hash the password
    const credentials = {
      email,
      password: hashedPassword,
      name,
      surname,
      phoneNumber,
      isTutor
    };
    return this.http.post(this.Url + '/register/student', credentials, {
      ...this.httpOptions,
      responseType: 'text' // Set the response type to 'text'
    })
      .pipe(
        tap((response: any) => {
          console.log(response);
        }),
        catchError((error: HttpErrorResponse) => {
          console.error('Register failed', error);
          return throwError(() => 'Register failed'); // Use throwError to create an error observable
        })
      );
  }

  isAuthenticated(): boolean {
    return this.studentLoggedIn || this.tutorLoggedIn;
  }

  isTutorLoggedIn(): boolean {
    return this.tutorLoggedIn;
  }

  isStudentLoggedIn(): boolean {
    return this.studentLoggedIn;
  }

  logout() {
    this.studentLoggedIn = false;
    this.tutorLoggedIn = false;
  }

  private hashPassword(password: string): string {
    return CryptoJS.SHA256(password).toString(CryptoJS.enc.Base64);  // Hash password with SHA-256
  }
}
