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

  constructor(private http: HttpClient) {
    this.displayCalendar();
  }

  ngOnInit(): void {
    this.getDatesTwoWeeksFromNow();
    this.generateTimeSlots();
  }

  displayCalendar() {
    const username = 'janir';
    const password = 'root';
    const credentials = btoa(username + ':' + password); // Encode the username and password

    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Authorization': 'Basic ' + credentials // Add the Basic Authentication header
      })
    };

    // this.http.get('http://localhost:8080/tutor/1/calendar' + this.formData.id + '/calendar/c0cc6a538c4604e5570b325de0095a2e9c1647adfc9c4e5f7bbc5efb71c5db57@group.calendar.google.com', httpOptions)
    this.http.get('http://localhost:8080/reservations/tutor/1/calendar/c0cc6a538c4604e5570b325de0095a2e9c1647adfc9c4e5f7bbc5efb71c5db57@group.calendar.google.com', httpOptions)
    .subscribe(response => {
      this.responseData = response; // Assign the response data to the variable
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

  generateTimeSlots() {
    const startTime = new Date();
    startTime.setHours(10, 0, 0); // Set start time to 10:00 AM
    const endTime = new Date();
    endTime.setHours(20, 0, 0); // Set end time to 8:00 PM

    const timeSlotDurationMinutes = 15;
    const timeSlotCount = Math.floor(
      (endTime.getTime() - startTime.getTime()) / (timeSlotDurationMinutes * 60 * 1000)
    );

    for (let i = 0; i <= timeSlotCount; i++) {
      const time = new Date(startTime.getTime() + i * timeSlotDurationMinutes * 60 * 1000);
      const formattedTime = time.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
      this.timeSlots.push(formattedTime);
    }
  }

}
