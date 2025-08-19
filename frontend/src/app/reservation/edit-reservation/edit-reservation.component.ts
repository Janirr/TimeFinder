import {Component, OnInit} from '@angular/core';
import {HttpService} from 'src/app/http.service';

@Component({
  selector: 'app-edit-reservation',
  templateUrl: './edit-reservation.component.html',
  styleUrls: ['./edit-reservation.component.css'],
  standalone: false
})

export class EditReservationComponent implements OnInit {
  responseData: any;
  event: any;
  eventId = '60pjecb16di6abb46cp30b9kc9h3abb26lij2b9oc9hj8c1k74qj0e1p60';
  lessonTime: number = 60;
  tutor: any = null;
  price: number = 0;
  note: string = '';

  constructor(private http: HttpService) {
  }

  ngOnInit(): void {
    this.displayEvent();
  }

  displayEvent() {
    this.http.get<any>(`'/reservations/event/${this.eventId}`)
      .subscribe(response => {
        this.event = response;
        this.tutor = response.tutor;
        this.price = response.price;
        this.lessonTime = response.duration;
        console.log(response);
      });
  }

  onSubmit() {
    // TODO: Implement form submission
    console.log('Form submitted', this.note);
  }

  onCancel() {
    // TODO: Implement cancellation logic
    console.log('Cancelled');
  }

  formatDateTime(dateTime: string | number): string {
    let timestamp: number;

    if (typeof dateTime === 'string') {
      timestamp = Number(dateTime);
    } else {
      timestamp = dateTime;
    }

    if (isNaN(timestamp)) {
      return '';
    }

    const dateObj = new Date(timestamp);
    const year = dateObj.getFullYear();
    const month = ("0" + (dateObj.getMonth() + 1)).slice(-2);
    const day = ("0" + dateObj.getDate()).slice(-2);
    const hours = ("0" + dateObj.getHours()).slice(-2);
    const minutes = ("0" + dateObj.getMinutes()).slice(-2);

    return `${day}-${month}-${year} ${hours}:${minutes}`;
  }


}
