import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PricingComponent } from './pricing/pricing.component';
import { ContactComponent } from './contact/contact.component';
import { ReservationsComponent } from './reservations/reservations.component';
import { MainPageComponent } from './main-page/main-page.component';
import { EditReservationComponent } from './reservation/edit-reservation/edit-reservation.component';
import { LoginComponent } from './login/login.component';
import { ShowCalendarComponent } from './calendar/show-calendar/show-calendar.component';

const routes: Routes = [
  { path: 'pricing', component: PricingComponent },
  { path: 'calendar', component: ShowCalendarComponent },
  { path: 'contact', component: ContactComponent },
  { path: 'reservations', component: ReservationsComponent },
  { path: 'reservation', component: EditReservationComponent},
  { path: 'login', component: LoginComponent},
  { path: '', component: MainPageComponent } // Domyślna trasa
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
