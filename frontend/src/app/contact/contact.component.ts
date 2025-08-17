import {Component, OnInit} from '@angular/core';
import {AuthService} from "../auth.service";
import {HttpService} from "../http.service";
import {UserService} from "../user.service";

@Component({
  selector: 'app-contact',
  templateUrl: './contact.component.html',
  styleUrls: ['./contact.component.css'],
  standalone: false
})
export class ContactComponent implements OnInit {
  tutorId: any;
  pricings: any;
  tutors: any;
  formSubmitted = false;
  tutor: any;

  constructor(private authService: AuthService, private httpService: HttpService, private userService: UserService) {
  }

  ngOnInit() {
    this.getTutors();
  }

  getTutorDetails(tutorId: number) {
    this.httpService.get(`/tutors/${tutorId}`).subscribe((response: any) => {
      this.tutor = response;
    });
  }

  getTutors() {
    this.httpService.get(`/tutors`)
      .subscribe(response => {
        this.tutors = response; // Assign the response data to the variable
        console.log(response);
      });
  }

  onSubmitForm() {
    this.getTutorDetails(this.tutorId);
    this.formSubmitted = true;
  }
}
