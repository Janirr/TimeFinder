import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {PricingComponent} from './pricing/pricing.component';
import {ContactComponent} from './contact/contact.component';
import {ReservationsComponent} from './reservations/reservations.component';
import {EditReservationComponent} from './reservation/edit-reservation/edit-reservation.component';
import {LoginComponent} from './login/login.component';
import {ShowCalendarComponent} from './calendar/show-calendar/show-calendar.component';
import {AccountComponent} from "./account/account.component";
import {NotFoundComponent} from './not-found/not-found.component'; // Assuming you have a 404 component
import {AuthGuard} from './auth.guard';
import {RegisterComponent} from "./register/register.component"; // Import the guard

const routes: Routes = [
  {path: 'pricing', component: PricingComponent, canActivate: [AuthGuard]},
  {path: '', component: ShowCalendarComponent, canActivate: [AuthGuard]},
  {path: 'calendar', component: ShowCalendarComponent, canActivate: [AuthGuard]},
  {path: 'contact', component: ContactComponent, canActivate: [AuthGuard]}, // Protect this route
  {path: 'reservations', component: ReservationsComponent, canActivate: [AuthGuard]},  // Protect this route
  {path: 'reservation', component: EditReservationComponent, canActivate: [AuthGuard]}, // Protect this route
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'account', component: AccountComponent, canActivate: [AuthGuard]}, // Protect this route
  {path: '**', component: NotFoundComponent},  // Catch-all route for undefined paths
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
