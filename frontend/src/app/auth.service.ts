import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpHeaders} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {catchError, tap} from 'rxjs/operators';
import {UserService} from './user.service';
import {Router} from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  username = 'janir';
  password = 'root';
  credentials = btoa(this.username + ':' + this.password);
  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': 'Basic ' + this.credentials
    })
  };
  private studentLoggedIn: boolean = false;
  private tutorLoggedIn: boolean = false;
  private Url = 'http://localhost:8080';

  constructor(private http: HttpClient, private userService: UserService, private router: Router) {
  }

  login(email: string, password: string): Observable<any> {
    const credentials = {email, password};
    return this.http.post(this.Url + '/login/student', credentials, {
      ...this.httpOptions,
      responseType: 'text'
    }).pipe(
      tap(async (response: any) => {
        if (response !== null && response !== 'UNAUTHORIZED') {
          const student = JSON.parse(response);
          console.log(student);
          this.studentLoggedIn = true;
          this.userService.student = student;
          localStorage.setItem('token', student.token); // Store token
          await this.router.navigate(['/calendar']); // Redirect student to home
        } else {
          this.studentLoggedIn = false;
        }
      }),
      catchError((error: HttpErrorResponse) => {
        console.error('Login failed', error);
        return throwError(() => 'Login failed');
      })
    );
  }

  tutorLogin(email: string, password: string): Observable<any> {
    const credentials = {email, password};
    return this.http.post(this.Url + '/login/tutor', credentials, {
      ...this.httpOptions,
      responseType: 'text'
    }).pipe(
      tap(async (response: any) => {
        if (response !== null && response !== 'UNAUTHORIZED') {
          const tutor = JSON.parse(response);
          console.log(tutor);
          this.tutorLoggedIn = true;
          this.userService.tutor = tutor;
          localStorage.setItem('token', tutor.token); // Store token
          this.router.navigate(['/calendar']); // Navigate to login on success
        } else {
          this.tutorLoggedIn = false;
        }
      }),
      catchError((error: HttpErrorResponse) => {
        console.error('Login failed', error);
        return throwError(() => 'Login failed');
      })
    );
  }

  logout() {
    this.studentLoggedIn = false;
    this.tutorLoggedIn = false;
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }

  register(email: string, password: string, name: string, surname: string, phoneNumber: number, isTutor: boolean): Observable<any> {
    const credentials = {email, password, name, surname, phoneNumber, isTutor};
    return this.http.post(this.Url + '/register', credentials, {
      ...this.httpOptions,
      responseType: 'text'
    }).pipe(
      tap((response: any) => {
        console.log(response);
      }),
      catchError((error: HttpErrorResponse) => {
        console.error('Register failed', error);
        return throwError(() => 'Register failed');
      })
    );
  }

  isAuthenticated(): boolean {
    return this.studentLoggedIn || this.tutorLoggedIn || this.userService.student != null || this.userService.tutor != null;
  }

  isTutorLoggedIn(): boolean {
    return this.tutorLoggedIn;
  }

  isStudentLoggedIn(): boolean {
    return this.studentLoggedIn;
  }
}
