import { Component, OnInit } from '@angular/core';
import { HttpService } from '../../http.service';
import { UserService } from '../../user.service';
import { waitForAsync } from '@angular/core/testing';

@Component({
  selector: 'app-show-calendar',
  templateUrl: './show-calendar.component.html',
  styleUrls: ['./show-calendar.component.css']
})
export class ShowCalendarComponent implements OnInit {
  calendarDates: any;
  tutor: any;
  uniqueDates: Array<any> = [];
  tutors: any;
  reservations: any;
  lessonTime = 60;
  lessonTimes = [45,60,90,120];
  tutorId: number = 1;
  calendarId: string = '';
  startDateTime: any;
  formSubmitted: boolean = false;

  constructor(private httpService: HttpService, public userService: UserService) {
    // this.displayCalendar();
    this.getTutors();
  }

  ngOnInit(): void {
    this.getDatesTwoWeeksFromNow();
  }

  onSubmitForm() {
    // Set the flag to true when the form is submitted
    this.formSubmitted = true;
    this.displayCalendar();
  }

  displayCalendar() {
    this.httpService.get(`/tutors/${this.tutorId}`)
    .subscribe(response => {
      this.tutor = response;
      this.tutorId = this.tutor.id;
      this.calendarId = this.tutor.calendarId;
      this.httpService.get(`/reservations/tutor/${this.tutorId}/calendar/${this.calendarId}/${this.lessonTime}`)
      .subscribe(response => {
        this.calendarDates = response; // Assign the response data to the variable
        console.log(response);
        // console.log(this.calendarId);
      });
    });

  }

  getTutors() {
    this.httpService.get(`/tutors`)
    .subscribe(response => {
      this.tutors = response; // Assign the response data to the variable
      // console.log(response);
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
    const dateComponents = startDateTime.date.split('T')[0];
    const startTime = startDateTime.fromHour.split(':');
    const endTime = startDateTime.untilHour.split(':');

    // console.log('Date Components:', dateComponents);
    // console.log('Start Time:', startTime);
    // console.log('End Time:', endTime);

    const startDate = new Date(dateComponents);
    startDate.setHours(parseInt(startTime[0], 10));
    startDate.setMinutes(parseInt(startTime[1], 10));
    startDate.setSeconds(parseInt(startTime[2], 10));

    const endDate = new Date(dateComponents);
    endDate.setHours(parseInt(endTime[0], 10));
    endDate.setMinutes(parseInt(endTime[1], 10));
    endDate.setSeconds(parseInt(endTime[2], 10));

    const event = {
      summary: 'Korepetycje',
      start: startDate,
      end: endDate,
      attendee: this.userService.student.email
    };

    const URL = `/reservations/tutor/${this.tutorId}/calendar/${this.calendarId}`;
    console.log(event);
    this.httpService.post(URL, event)
      .subscribe(response => {
        console.log('Reservation added successfully:', response);
      }, error => {
        console.error('Error adding reservation:', error);
      });
  }

  confirmReservation(startDateTime: any) {
    console.log(startDateTime);
    const confirmation = window.confirm('Czy na pewno chcesz dokonać rezerwacji od ' + startDateTime.fromHour + ' do ' + startDateTime.untilHour + '?');

    if (confirmation) {
      this.addReservationToCalendar(startDateTime);
    }
  }
}
