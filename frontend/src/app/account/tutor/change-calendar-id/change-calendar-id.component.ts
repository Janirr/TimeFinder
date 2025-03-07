import {Component, OnInit} from '@angular/core';
import {AuthService} from "../../../auth.service";
import {HttpService} from "../../../http.service";
import {UserService} from "../../../user.service";

@Component({
  selector: 'app-change-calendar-id',
  templateUrl: './change-calendar-id.component.html',
  styleUrls: ['./change-calendar-id.component.css']
})
export class ChangeCalendarIdComponent implements OnInit {
  calendarId: string = ''
  possibleCalendarIds: any = [];

  constructor(private authService: AuthService, private httpService: HttpService, private userService: UserService) {
  }

  ngOnInit() {
    this.calendarId = this.userService.tutor.calendarId;
    this.getPossibleCalendarIds();
  }

  authorize() {
    this.httpService.getAuthorizationUrl(this.userService.tutor.id).subscribe(response => {
      window.open(response.authUrl, '_blank'); // Opens in a new tab
    });
  }


  changeCalendarId() {
    this.httpService.put(`/tutors/${this.userService.tutor.id}/calendar/${this.calendarId}`).subscribe((response) => {
      console.log(response);
    });
  }

  getPossibleCalendarIds() {
    this.httpService.get(`/tutors/${this.userService.tutor.id}/calendars`).subscribe((response) => {
      this.possibleCalendarIds = response;
    });
  }
}
