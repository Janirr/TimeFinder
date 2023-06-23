import { Component, OnInit } from '@angular/core';
import { HttpService } from '../http.service';
import { UserService } from '../user.service';

@Component({
  selector: 'app-reservations',
  templateUrl: './reservations.component.html',
  styleUrls: ['./reservations.component.css']
})
export class ReservationsComponent implements OnInit {
  reservations: any;
  tutorId: number = 1;
  calendarId: string = 'c0cc6a538c4604e5570b325de0095a2e9c1647adfc9c4e5f7bbc5efb71c5db57@group.calendar.google.com';

  constructor(private httpService: HttpService, public userService: UserService) {
    this.displayCalendarEvents();
  }

  ngOnInit(): void {
  }

  onSubmitForm() {
    this.displayCalendarEvents();
  }
  displayCalendarEvents() {
    this.httpService.get(`/reservations/google/tutor/${this.tutorId}/calendar/${this.calendarId}`)
      .subscribe(response => {
        this.reservations = response; // Assign the response data to the variable
        console.log(response);
      });
  }
}

