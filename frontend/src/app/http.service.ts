import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class HttpService {
  private username = 'janir';
  private password = 'root';
  private credentials = btoa(this.username + ':' + this.password); // Encode the username and password
  private urlTemplate = 'http://localhost:8080';
  private httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': 'Basic ' + this.credentials // Add the Basic Authentication header
    })
  };

  constructor(private http: HttpClient) {
  }

  get<T>(url: string) {
    return this.http.get<T>(this.urlTemplate + url, this.httpOptions);
  }

  post(url: string, data: any) {
    return this.http.post(this.urlTemplate + url, data, this.httpOptions);
  }

  patch(url: string, data: any) {
    return this.http.patch(this.urlTemplate + url, data, this.httpOptions);
  }

  delete(url: string) {
    return this.http.delete(this.urlTemplate + url, this.httpOptions);
  }

  put(url: string) {
    return this.http.put(this.urlTemplate + url, null, this.httpOptions);
  }

  getAuthorizationUrl(tutorId: number) {
    return this.http.get<{ authUrl: string }>(`${this.urlTemplate}/calendar/authorize/${tutorId}`, this.httpOptions);
  }

  getTutorReservations() {
    return this.get<any[]>('/reservations');
  }

  getStudentReservations() {
    return this.get<any[]>('/reservations/student');
  }

  cancelReservation(id: string) {
    return this.delete(`/reservations/${id}`);
  }

  updateReservation(id: string, data: any) {
    return this.patch(`/reservations/${id}`, data);
  }
}
