import {Component, OnInit} from '@angular/core';
import {HttpService} from '../../http.service';
import {UserService} from '../../user.service';
import {AvailableTime, CalendarResponse, LESSON_TIME, LESSON_TIMES, TUTOR_ID} from './calendar.model';
import {Observable, tap} from 'rxjs';

@Component({
  selector: 'app-show-calendar',
  templateUrl: './show-calendar.component.html',
  styleUrls: ['./show-calendar.component.css']
})

export class ShowCalendarComponent implements OnInit {

  calendarDates = new Map<string, CalendarResponse>();
  tutor: any;
  uniqueDates: Array<any> = [];
  tutors: any;
  reservations: any;
  lessonTime = LESSON_TIME;
  lessonTimes = LESSON_TIMES;
  tutorId = TUTOR_ID;
  calendarId = '';
  startDateTime: any;
  formSubmitted = false;

  constructor(private httpService: HttpService, public userService: UserService) {
    // this.displayCalendar();
    this.getTutors();
  }

  ngOnInit(): void {
    this.getDatesTwoWeeksFromNow();
  }

  async onSubmitForm() {
    // Set the flag to true when the form is submitted
    this.formSubmitted = true;

    await this.getTutorCalendar().toPromise(); // Wait for getTutorCalendar to complete

    this.getCalendar();
  }

  getTutorCalendar(): Observable<any> {
    return this.httpService.get(`/tutors/${this.tutorId}`)
      .pipe(
        tap((response2: any) => {
          this.tutor = response2;
          this.tutorId = this.tutor.id;
          this.calendarId = this.tutor.calendarId;
        })
      );
  }

  getCalendar() {
    this.httpService.get(`/reservations/tutor/${this.tutorId}/calendar/${this.calendarId}/${this.lessonTime}`)
      .subscribe((calendarResponse: any) => {
        // Explicitly cast the response to CalendarResponse
        this.calendarDates = new Map(Object.entries(calendarResponse));
        console.log(this.calendarDates);
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

  addReservationToCalendar(timestamp: AvailableTime, date: Date) {
    const startTime: string = timestamp.fromHour;
    const endTime: string = timestamp.untilHour;

    const startDate: Date = date;
    startDate.setHours(parseInt(startTime[0], 10));
    startDate.setMinutes(parseInt(startTime[1], 10));
    startDate.setSeconds(parseInt(startTime[2], 10));

    const endDate: Date = date;
    endDate.setHours(parseInt(endTime[0], 10));
    endDate.setMinutes(parseInt(endTime[1], 10));
    endDate.setSeconds(parseInt(endTime[2], 10));

    const event = {
      summary: 'Korepetycje',
      start: new Date(startTime),
      end: new Date(endTime),
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

  confirmReservation(startDateTime: any, date: string) {
    console.log(startDateTime);
    const confirmation = window.confirm('Czy na pewno chcesz dokonać rezerwacji od ' + startDateTime.fromHour + ' do ' + startDateTime.untilHour + '?');

    if (confirmation) {
      this.addReservationToCalendar(startDateTime, new Date(date));
    }
  }
}
