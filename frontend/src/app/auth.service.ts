import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { tap, catchError } from 'rxjs/operators';
import { UserService } from './user.service';

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
  private Url = 'http://timefind.eu-central-1.elasticbeanstalk.com/login'; // Update with your API URL
  private studentLoggedIn: boolean = false;
  private tutorLoggedIn: boolean = false;

  constructor(private http: HttpClient, private userService: UserService) {}

  login(email: string, password: string): Observable<any> {
    const credentials = {
      email,
      password
    };
    return this.http.post(this.Url + '/student', credentials, {
      ...this.httpOptions,
      responseType: 'text' // Set the response type to 'text'
    })
      .pipe(
        tap((response: any) => {
          if (response !== null && response !== 'UNAUTHORIZED') {
            const student = JSON.parse(response);
            console.log(student)
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
    const credentials = {
      email,
      password
    };
    return this.http.post(this.Url + '/tutor', credentials, {
      ...this.httpOptions,
      responseType: 'text' // Set the response type to 'text'
    })
      .pipe(
        tap((response: any) => {
          if (response !== null && response !== 'UNAUTHORIZED') {
            const tutor = JSON.parse(response);
            console.log(tutor)
            this.tutorLoggedIn = true;
            this.userService.tutor = tutor; // Assign the response string directly to email
          } else {
            this.tutorLoggedIn = false;
          }
        }),
        catchError((error: HttpErrorResponse) => {
          console.error('Login failed', error);
          return throwError('Login failed'); // Use throwError to create an error observable
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

  logout(){
    this.studentLoggedIn = false;
    this.tutorLoggedIn = false;
  }
}
