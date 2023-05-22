import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Injectable } from '@angular/core';


@Component({
  selector: 'app-show-calendar',
  templateUrl: './show-calendar.component.html',
  styleUrls: ['./show-calendar.component.css']
})
@Injectable()
export class ShowCalendarComponent implements OnInit {
  formData = {id: 1};
  responseData: any;
  uniqueDates: Array<any> = [];
  timeSlots: string[] = [];
  reservations: any;

  constructor(private http: HttpClient) {
    this.displayCalendar();
    this.displayCalendarEvents();
  }

  ngOnInit(): void {
    this.getDatesTwoWeeksFromNow();
  }

  displayCalendar() {
    const username = 'janir';
    const password = 'root';
    const credentials = btoa(username + ':' + password); // Encode the username and password
    const urlTemplate =  'http://localhost:8080';
    const calendarId = 'c0cc6a538c4604e5570b325de0095a2e9c1647adfc9c4e5f7bbc5efb71c5db57@group.calendar.google.com';
    const minutesForLesson = 90;
    const tutorId = 1;

    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Authorization': 'Basic ' + credentials // Add the Basic Authentication header
      })
    };

    this.http.get(urlTemplate + '/reservations/tutor/' + tutorId + '/calendar/' + calendarId + '/' + minutesForLesson, httpOptions)
    .subscribe(response => {
      this.responseData = response; // Assign the response data to the variable
      console.log(response);
   });
  }

  displayCalendarEvents(){
    const username = 'janir';
    const password = 'root';
    const credentials = btoa(username + ':' + password); // Encode the username and password
    const urlTemplate =  'http://localhost:8080';
    const calendarId = 'c0cc6a538c4604e5570b325de0095a2e9c1647adfc9c4e5f7bbc5efb71c5db57@group.calendar.google.com';
    const tutorId = 1;

    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Authorization': 'Basic ' + credentials // Add the Basic Authentication header
      })
    };

    this.http.get(urlTemplate + '/reservations/google/tutor/'+ tutorId +'/calendar/' + calendarId, httpOptions)
    .subscribe(response => {
      this.reservations = response; // Assign the response data to the variable
      console.log(response);
   });
  }

  getDatesTwoWeeksFromNow() {
    const today = new Date();
    const twoWeeksLater = new Date(today.getTime() + 14 * 24 * 60 * 60 * 1000); // Add 14 days in milliseconds

    const dates: Array<any> = [];
    const currentDay = today;
    while (currentDay < twoWeeksLater) {
      const day = currentDay.getDate();
      const month = currentDay.getMonth();
      const dayAndMonth = {
        day: day,
        month: month
      };
      dates.push(dayAndMonth);
      currentDay.setDate(currentDay.getDate() + 1); // Move to the next day
    }
    this.uniqueDates = dates;
  }

}
