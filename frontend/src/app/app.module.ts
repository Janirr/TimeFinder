import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { ShowCalendarComponent } from './calendar/show-calendar/show-calendar.component';
import { MakeReservationComponent } from './reservation/make-reservation/make-reservation.component';
import { EditReservationComponent } from './reservation/edit-reservation/edit-reservation.component';
import { FormsModule } from '@angular/forms';
import { PricingComponent } from './pricing/pricing.component';
import { ContactComponent } from './contact/contact.component';
import { ReservationsComponent } from './reservations/reservations.component';
import { MainPageComponent } from './main-page/main-page.component';
import { LoginComponent } from './login/login.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    ShowCalendarComponent,
    MakeReservationComponent,
    EditReservationComponent,
    PricingComponent,
    ContactComponent,
    ReservationsComponent,
    MainPageComponent,
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
