import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable, Output } from '@angular/core';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class HttpServiceService {
  @Output() loadedCalendar: any;
  @Output() busyTime: Array<any> = [];
  @Output() freeTime: Array<any> = [];
  @Output() event: any;
  @Output() element: any;

  constructor(private http: HttpClient) { }

  username = 'janir';
  password = 'root';
  credentials = btoa(this.username + ':' + this.password); // Encode the username and password

  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': 'Basic ' + this.credentials // Add the Basic Authentication header
    })
  };

  showCalendar() {
    this.http.get('http://localhost:8080/tutors/1/calendar/c0cc6a538c4604e5570b325de0095a2e9c1647adfc9c4e5f7bbc5efb71c5db57@group.calendar.google.com', this.httpOptions)
    .pipe(map(responseData => {

    }))
    .subscribe(response => {
      this.event = response;
        const startDate = new Date(this.event.start.dateTime.value);
        const endDate = new Date(this.event.end.dateTime.value);
        const summary = this.event.summary;
        const id = this.event.id;
        const transparency = this.event.transparency;

        this.element = {
          id: id,
          startDate: startDate,
          endDate: endDate,
          summary: summary
        };
      });
    };

  showEvent(){
    this.http.get('http://localhost:8080/reservations/calendar/c0cc6a538c4604e5570b325de0095a2e9c1647adfc9c4e5f7bbc5efb71c5db57@group.calendar.google.com/event/60pjecb16di6abb46cp30b9kc9h3abb26lij2b9oc9hj8c1k74qj0e1p60', this.httpOptions)
    .pipe(map(responseData => {

    }))
    .subscribe(response => {
      this.loadedCalendar = response;
      console.log(this.loadedCalendar);

      this.loadedCalendar.forEach((item: any) => {
        const startDate = new Date(item.start.dateTime.value);
        const endDate = new Date(item.end.dateTime.value);
        const summary = item.summary;
        const id = item.id;
        const transparency = item.transparency;

        const element = {
          id: id,
          startDate: startDate,
          endDate: endDate,
          summary: summary
        };

        if (transparency === 'transparent') {
          this.freeTime.push(element);
        } else {
          this.busyTime.push(element);
        }
      });
    });
  }
}
