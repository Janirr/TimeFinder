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

  constructor(private http: HttpClient) { }

  showCalendar() {

    const username = 'janir';
    const password = 'root';
    const credentials = btoa(username + ':' + password); // Encode the username and password

    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Authorization': 'Basic ' + credentials // Add the Basic Authentication header
      })
    };

    this.http.get('http://localhost:8080/tutors/1/calendar/c0cc6a538c4604e5570b325de0095a2e9c1647adfc9c4e5f7bbc5efb71c5db57@group.calendar.google.com', httpOptions)
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
