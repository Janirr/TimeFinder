import {AuthService} from '../auth.service';
import {Component, OnInit} from '@angular/core';
import {HttpService} from '../http.service';
import {UserService} from '../user.service';
import {MatTableDataSource} from '@angular/material/table';
import {MatDialog} from '@angular/material/dialog';
import {MatSnackBar} from '@angular/material/snack-bar';
import {ConfirmationDialogComponent} from './confirmation-dialog/confirmation-dialog.component';

@Component({
  selector: 'app-reservations',
  templateUrl: './reservations.component.html',
  styleUrls: ['./reservations.component.css'],
  standalone: false
})
export class ReservationsComponent implements OnInit {
  reservations: any;
  studentReservations: any;
  tutorId: number = 1;
  calendarId: string = 'c0cc6a538c4604e5570b325de0095a2e9c1647adfc9c4e5f7bbc5efb71c5db57@group.calendar.google.com';
  displayedColumns: string[] = ['date', 'startTime', 'endTime', 'description', 'actions'];
  dataSource = new MatTableDataSource<any>([]);

  constructor(private httpService: HttpService, public userService: UserService, public authService: AuthService, private snackBar: MatSnackBar, private dialog: MatDialog) {
    if (this.authService.isTutorLoggedIn()) {
      this.displayTutorCalendarEvents();
    } else {
      this.displayStudentReservations();
    }
  }

  ngOnInit(): void {
    this.loadReservations();
  }

  onSubmitForm() {
    if (this.authService.isTutorLoggedIn()) {
      this.displayTutorCalendarEvents();
    } else {
      this.displayStudentReservations();
    }
  }

  displayTutorCalendarEvents() {
    this.httpService.get(`/reservations/google/tutor/${this.tutorId}/calendar/${this.calendarId}`)
      .subscribe(response => {
        this.reservations = response;
        console.log(response);
      });
  }

  displayStudentReservations() {
    const email = this.userService.student.email;
    this.httpService.get(`/reservations?email=${email}`)
      .subscribe(response => {
        this.studentReservations = response;
        console.log(response);
      });
  }

  loadReservations() {
    if (this.authService.isTutorLoggedIn()) {
      this.loadTutorReservations();
    } else {
      this.loadStudentReservations();
    }
  }

  cancelReservation(reservation: any) {
    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      width: '400px',
      data: {message: 'Czy na pewno chcesz anulować tę rezerwację?'}
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.httpService.delete(`/reservations/${reservation.id}`).subscribe({
          next: () => {
            this.snackBar.open('Rezerwacja została anulowana', 'OK', {
              duration: 3000,
              horizontalPosition: 'end',
              verticalPosition: 'top'
            });
            this.loadReservations();
          },
          error: () => {
            this.snackBar.open('Nie udało się anulować rezerwacji', 'OK', {
              duration: 3000,
              horizontalPosition: 'end',
              verticalPosition: 'top',
              panelClass: ['error-snackbar']
            });
          }
        });
      }
    });
  }

  synchronizeWithGoogleCalendar() {
    if (this.authService.isStudentLoggedIn()) {
      this.httpService.get(`/reservations/tutor/${this.tutorId}/calendar/${this.calendarId}/student/${this.userService.student.email}`).subscribe(
        response => {
          console.log(response);
          this.ngOnInit()
        }
      );
    } else {
      this.httpService.get(`/reservations/tutor/${this.tutorId}/calendar/${this.calendarId}`).subscribe(
        response => {
          console.log(response);
          this.ngOnInit()
        }
      );
    }
  }

  private loadTutorReservations() {
    this.httpService.get(`tutors/${this.tutorId}/calendars/events`).subscribe({
      next: (response: any) => {
        this.handleReservationsResponse(response);
      },
      error: this.handleError.bind(this)
    });
  }

  private loadStudentReservations() {
    this.httpService.get(`students/${this.userService.student.id}/reservations`).subscribe({
      next: (response: any) => {
        this.handleReservationsResponse(response);
      },
      error: this.handleError.bind(this)
    });
  }

  private handleReservationsResponse(response: any[]) {
    this.reservations = response.filter(res => res.transparency !== 'transparent');
    this.dataSource.data = this.reservations;
  }

  private handleError(error: any) {
    this.snackBar.open('Wystąpił błąd podczas ładowania rezerwacji', 'OK', {
      duration: 3000,
      horizontalPosition: 'end',
      verticalPosition: 'top',
      panelClass: ['error-snackbar']
    });
  }
}
