import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, Injectable, OnInit } from '@angular/core';

@Component({
  selector: 'app-single-event',
  templateUrl: './single-event.component.html',
  styleUrls: ['./single-event.component.css']
})
@Injectable()
export class SingleEventComponent implements OnInit {
  responseData: any;
  event: any;

  constructor(private http: HttpClient) {
  }

  ngOnInit(): void {
    this.displayEvent();
  }

  displayEvent() {
    const username = 'janir';
    const password = 'root';
    const credentials = btoa(username + ':' + password); // Encode the username and password
    const urlTemplate =  'http://localhost:8080';
    const calendarId = 'c0cc6a538c4604e5570b325de0095a2e9c1647adfc9c4e5f7bbc5efb71c5db57@group.calendar.google.com';
    const eventId = '60pjecb16di6abb46cp30b9kc9h3abb26lij2b9oc9hj8c1k74qj0e1p60';

    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Authorization': 'Basic ' + credentials // Add the Basic Authentication header
      })
    };

    this.http.get(urlTemplate + '/reservations/calendar/' + calendarId + '/event/' + eventId, httpOptions)
    .subscribe(response => {
      this.event = response; // Assign the response data to the variable
      console.log(response);
   });

  }

}
