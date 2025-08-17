import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-show-pricing',
  templateUrl: './show-pricing.component.html',
  styleUrls: ['./show-pricing.component.css'],
  standalone: false
})
export class ShowPricingComponent implements OnInit {
  @Input() pricings!: any;

  ngOnInit() {
  }

}
