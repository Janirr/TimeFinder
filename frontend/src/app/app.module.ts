import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { ShowCalendarComponent } from './calendar/show-calendar/show-calendar.component';
import { EditReservationComponent } from './reservation/edit-reservation/edit-reservation.component';
import { FormsModule } from '@angular/forms';
import { PricingComponent } from './pricing/pricing.component';
import { ContactComponent } from './contact/contact.component';
import { ReservationsComponent } from './reservations/reservations.component';
import { LoginComponent } from './login/login.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    ShowCalendarComponent,
    EditReservationComponent,
    PricingComponent,
    ContactComponent,
    ReservationsComponent,
    LoginComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
