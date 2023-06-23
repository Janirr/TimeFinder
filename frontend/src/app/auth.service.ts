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
  private Url = 'http://localhost:8080/login'; // Update with your API URL
  private loggedIn: boolean = false;

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
            this.loggedIn = true;
            this.userService.email = student.email; // Assign the response string directly to email
          } else {
            this.loggedIn = false;
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
            this.loggedIn = true;
            this.userService.email = tutor.email; // Assign the response string directly to email
          } else {
            this.loggedIn = false;
          }
        }),
        catchError((error: HttpErrorResponse) => {
          console.error('Login failed', error);
          return throwError('Login failed'); // Use throwError to create an error observable
        })
      );
  }

  isAuthenticated(): boolean {
    return this.loggedIn;
  }

  logout(){
    this.loggedIn = false;
  }
}
