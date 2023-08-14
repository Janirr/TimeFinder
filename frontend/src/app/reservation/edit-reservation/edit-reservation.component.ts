import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, Injectable, OnInit } from '@angular/core';

@Component({
  selector: 'app-edit-reservation',
  templateUrl: './edit-reservation.component.html',
  styleUrls: ['./edit-reservation.component.css']
})

export class EditReservationComponent implements OnInit {
  responseData: any;
  event: any;
  eventId = '60pjecb16di6abb46cp30b9kc9h3abb26lij2b9oc9hj8c1k74qj0e1p60';

  constructor(private http: HttpClient) {
  }

  ngOnInit(): void {
    this.displayEvent();
  }

  displayEvent() {
    const username = 'janir';
    const password = 'root';
    const credentials = btoa(username + ':' + password); // Encode the username and password
    const urlTemplate =  'http://timefind.eu-central-1.elasticbeanstalk.com';
    const calendarId = 'c0cc6a538c4604e5570b325de0095a2e9c1647adfc9c4e5f7bbc5efb71c5db57@group.calendar.google.com';

    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Authorization': 'Basic ' + credentials // Add the Basic Authentication header
      })
    };

    this.http.get(urlTemplate + '/reservations/calendar/' + calendarId + '/event/' + this.eventId, httpOptions)
    .subscribe(response => {
      this.event = response; // Assign the response data to the variable
      console.log(response);
   });
  }
  formatDateTime(dateTime: string | number): string {
    let timestamp: number;

    if (typeof dateTime === 'string') {
      timestamp = Number(dateTime);
    } else if (typeof dateTime === 'number') {
      timestamp = dateTime;
    } else {
      return ''; // Return an empty string or handle the error as per your requirements
    }

    if (isNaN(timestamp)) {
      return ''; // Return an empty string or handle the error as per your requirements
    }

    const dateObj = new Date(timestamp);
    const year = dateObj.getFullYear();
    const month = ("0" + (dateObj.getMonth() + 1)).slice(-2);
    const day = ("0" + dateObj.getDate()).slice(-2);
    const hours = ("0" + dateObj.getHours()).slice(-2);
    const minutes = ("0" + dateObj.getMinutes()).slice(-2);

    return `${year}-${month}-${day}T${hours}:${minutes}`;
  }



}
