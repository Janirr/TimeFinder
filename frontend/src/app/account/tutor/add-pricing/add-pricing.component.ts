import {Component, OnInit} from '@angular/core';
import {UserService} from "../../../user.service";
import {HttpService} from "../../../http.service";

@Component({
  selector: 'app-add-pricing',
  templateUrl: './add-pricing.component.html',
  styleUrls: ['./add-pricing.component.css'],
  standalone: false
})
export class AddPricingComponent implements OnInit {
  pricings: any = []
  tutorId: any;

  constructor(private userService: UserService, private httpService: HttpService) {

  }

  ngOnInit() {
    this.tutorId = this.userService.tutor.id;
    this.getPricings(this.tutorId);
  }

  getPricings(tutorId: number) {
    this.httpService.get(`/tutors/${tutorId}/pricings`).subscribe((response: any) => {
      this.pricings = response;
    });
  }

  onFormSubmit() {
    console.log(this.pricings);
  }

  addNewPricing() {
    this.pricings.push({level: '', price: '', tutor_id: this.tutorId}); // Push a new pricing object with default values
  }

  deletePricing(index: any) {
    this.pricings.splice(index, 1);
  }

  ngOnChanges() {
    this.getPricings(this.tutorId);
  }

  acceptNewPricings() {
    this.httpService.post(`/tutors/${this.tutorId}/pricings`, this.pricings).subscribe((response: any) => {
      console.log(response);
    });
  }
}
