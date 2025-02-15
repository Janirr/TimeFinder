import {LOCALE_ID, NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {HttpClientModule} from '@angular/common/http';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HeaderComponent} from './header/header.component';
import {ShowCalendarComponent} from './calendar/show-calendar/show-calendar.component';
import {EditReservationComponent} from './reservation/edit-reservation/edit-reservation.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {PricingComponent} from './pricing/pricing.component';
import {ContactComponent} from './contact/contact.component';
import {ReservationsComponent} from './reservations/reservations.component';
import {LoginComponent} from './login/login.component';
import {registerLocaleData} from "@angular/common";
import localePl from '@angular/common/locales/pl';
import {TutorComponent} from './account/tutor/tutor.component';
import {AccountComponent} from './account/account.component';
import {StudentComponent} from './account/student/student.component';
import {ChangeCalendarIdComponent} from './account/tutor/change-calendar-id/change-calendar-id.component';
import {AddPricingComponent} from './account/tutor/add-pricing/add-pricing.component';
import {ShowPricingComponent} from './pricing/show-pricing/show-pricing.component';
import {RegisterComponent} from './register/register.component';
import {NotFoundComponent} from './not-found/not-found.component';

registerLocaleData(localePl);

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    ShowCalendarComponent,
    EditReservationComponent,
    PricingComponent,
    ContactComponent,
    ReservationsComponent,
    LoginComponent,
    TutorComponent,
    AccountComponent,
    StudentComponent,
    ChangeCalendarIdComponent,
    AddPricingComponent,
    ShowPricingComponent,
    RegisterComponent,
    NotFoundComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [{provide: LOCALE_ID, useValue: 'pl'}],
  bootstrap: [AppComponent]
})
export class AppModule {
}
