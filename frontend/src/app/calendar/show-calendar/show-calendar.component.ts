import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {MatSnackBar} from '@angular/material/snack-bar';
import {HttpService} from '../../http.service';
import {AuthService} from '../../auth.service';

@Component({
  selector: 'app-show-calendar',
  templateUrl: './show-calendar.component.html',
  styleUrls: ['./show-calendar.component.css'],
  standalone: false
})
export class ShowCalendarComponent implements OnInit {
  lessonTimes: number[] = [45, 60, 90, 120];
  lessonTime: number = 45;
  tutors: any[] = [];
  tutorId: string = '';
  events: any[] = [];
  formSubmitted = false;
  selectedEventId: string | null = null;
  calendarId = '';

  constructor(
    private httpService: HttpService,
    private router: Router,
    private authService: AuthService,
    private snackBar: MatSnackBar
  ) {
  }

  ngOnInit() {
    this.loadTutors();
  }

  loadTutors() {
    this.httpService.get(`/tutors`).subscribe({
      next: (response: any) => {
        this.tutors = response;
      },
      error: (error) => {
        this.snackBar.open('Nie udało się załadować listy nauczycieli', 'OK', {
          duration: 3000,
          horizontalPosition: 'end',
          verticalPosition: 'top'
        });
      }
    });
  }

  onSubmitForm() {
    if (!this.tutorId || !this.lessonTime) {
      this.snackBar.open('Wybierz nauczyciela i czas trwania zajęć', 'OK', {
        duration: 3000,
        horizontalPosition: 'end',
        verticalPosition: 'top'
      });
      return;
    }

    this.httpService.get(`/reservations/tutor/${this.tutorId}/${this.lessonTime}`).subscribe({
      next: (response: any) => {
        this.events = response;
        this.formSubmitted = true;
        if (this.events.length === 0) {
          this.snackBar.open('Brak dostępnych terminów dla wybranych parametrów', 'OK', {
            duration: 3000,
            horizontalPosition: 'end',
            verticalPosition: 'top'
          });
        }
      },
      error: (error) => {
        this.snackBar.open('Nie udało się załadować terminów', 'OK', {
          duration: 3000,
          horizontalPosition: 'end',
          verticalPosition: 'top'
        });
      }
    });
  }

  onEventClick(event: any) {
    if (!this.authService.isStudentLoggedIn()) {
      this.snackBar.open('Musisz się zalogować, aby zarezerwować termin', 'Zaloguj się', {
        duration: 5000,
        horizontalPosition: 'end',
        verticalPosition: 'top'
      }).onAction().subscribe(() => {
        this.router.navigate(['/login']);
      });
      return;
    }

    this.selectedEventId = event.id;
    this.router.navigate(['/reservation/edit'], {
      queryParams: {
        eventId: event.id,
        tutorId: this.tutorId,
        lessonTime: this.lessonTime
      }
    });
  }
}
