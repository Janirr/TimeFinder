import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {PricingComponent} from './pricing/pricing.component';
import {ContactComponent} from './contact/contact.component';
import {ReservationsComponent} from './reservations/reservations.component';
import {EditReservationComponent} from './reservation/edit-reservation/edit-reservation.component';
import {LoginComponent} from './login/login.component';
import {ShowCalendarComponent} from './calendar/show-calendar/show-calendar.component';
import {RegisterComponent} from "./register/register.component";

const routes: Routes = [
  {path: 'pricing', component: PricingComponent},
  {path: '', component: ShowCalendarComponent},
  {path: 'calendar', component: ShowCalendarComponent},
  {path: 'contact', component: ContactComponent},
  {path: 'reservations', component: ReservationsComponent},
  {path: 'reservation', component: EditReservationComponent},
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
