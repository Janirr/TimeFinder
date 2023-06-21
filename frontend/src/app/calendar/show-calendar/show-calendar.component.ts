import { UserService } from './../../user.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Injectable } from '@angular/core';


@Component({
  selector: 'app-show-calendar',
  templateUrl: './show-calendar.component.html',
  styleUrls: ['./show-calendar.component.css']
})
export class ShowCalendarComponent implements OnInit {
  formData = {id: 1};
  responseData: any;
  uniqueDates: Array<any> = [];
  timeSlots: string[] = [];
  reservations: any;
  minutesForLesson = 90;
  tutorId = 1;
  calendarId = 'c0cc6a538c4604e5570b325de0095a2e9c1647adfc9c4e5f7bbc5efb71c5db57@group.calendar.google.com';
  urlTemplate = 'http://localhost:8080';
  username = 'janir';
  password = 'root';
  credentials = btoa(this.username + ':' + this.password); // Encode the username and password
  startDateTime: any;


  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': 'Basic ' + this.credentials // Add the Basic Authentication header
    })
  };

  constructor(private http: HttpClient, public userService: UserService) {
    this.displayCalendar();
    this.displayCalendarEvents();
  }

  ngOnInit(): void {
    this.getDatesTwoWeeksFromNow();
  }

  displayCalendar() {
    this.http.get(this.urlTemplate + '/reservations/tutor/' + this.tutorId + '/calendar/' + this.calendarId + '/' + this.minutesForLesson, this.httpOptions)
    .subscribe(response => {
      this.responseData = response; // Assign the response data to the variable
      console.log(response);
   });
  }

  displayCalendarEvents(){
    this.http.get(this.urlTemplate + '/reservations/google/tutor/'+ this.tutorId +'/calendar/' + this.calendarId, this.httpOptions)
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

    addReservationToCalendar(startDateTime: any) {
      const dateComponents = startDateTime.date.split("T")[0];
      const startTime = startDateTime.fromHour.split(":");
      const endTime = startDateTime.untilHour.split(":");

      console.log("Date Components:", dateComponents);
      console.log("Start Time:", startTime);
      console.log("End Time:", endTime);

      const startDate = new Date(dateComponents);
      startDate.setHours(parseInt(startTime[0], 10));
      startDate.setMinutes(parseInt(startTime[1], 10));
      startDate.setSeconds(parseInt(startTime[2], 10));

      const endDate = new Date(dateComponents);
      endDate.setHours(parseInt(endTime[0], 10));
      endDate.setMinutes(parseInt(endTime[1], 10));
      endDate.setSeconds(parseInt(endTime[2], 10));

      console.log("Start Date:", startDate);
      console.log("End Date:", endDate);
      console.log("mail:"+this.userService.email);
      console.log("user:"+this.userService.username);

      const event = {
        summary: 'Korepetycje',
        start: startDate,
        end: endDate,
        attendee: this.userService.email
      };


      const URL = this.urlTemplate + '/reservations/tutor/' + this.tutorId + '/calendar/' + this.calendarId;
    console.log(event);
    this.http.post(URL, event, this.httpOptions)
      .subscribe(response => {
        console.log('Reservation added successfully:', response);
      }, error => {
        console.error('Error adding reservation:', error);
      });
    }

    confirmReservation(startDateTime: any) {
      console.log(startDateTime);
      const confirmation = window.confirm('Czy na pewno chcesz dokonać rezerwacji od '+startDateTime.fromHour+' do '+startDateTime.untilHour+'?');

      if (confirmation) {
        this.addReservationToCalendar(startDateTime);
      }
    }

}
