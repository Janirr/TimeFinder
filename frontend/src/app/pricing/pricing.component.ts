import {Component, OnInit} from '@angular/core';
import {AuthService} from "../auth.service";
import {HttpService} from "../http.service";
import {UserService} from "../user.service";

@Component({
  selector: 'app-pricing',
  templateUrl: './pricing.component.html',
  styleUrls: ['./pricing.component.css'],
  standalone: false
})
export class PricingComponent implements OnInit {
  tutorId: any;
  pricings: any;
  tutors: any;
  formSubmitted = false;

  constructor(private authService: AuthService, private httpService: HttpService, private userService: UserService) {
  }

  ngOnInit() {
    this.getTutors();
  }

  getPricings(tutorId: number) {
    this.httpService.get(`/tutors/${tutorId}/pricings`).subscribe((response: any) => {
      this.pricings = response;
    });
  }

  getTutors() {
    this.httpService.get(`/tutors`)
      .subscribe(response => {
        this.tutors = response; // Assign the response data to the variable
        // console.log(response);
      });
  }

  onSubmitForm() {
    this.getPricings(this.tutorId);
    this.formSubmitted = true;
  }
}
