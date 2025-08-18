import {Component, OnInit} from '@angular/core';
import {MatDialog} from '@angular/material/dialog';
import {MatSnackBar} from '@angular/material/snack-bar';
import {UserService} from '../../user.service';
import {HttpService} from '../../http.service';
import {EditProfileDialogComponent} from './edit-profile-dialog/edit-profile-dialog.component';

@Component({
  selector: 'app-student',
  templateUrl: './student.component.html',
  styleUrls: ['./student.component.css'],
  standalone: false
})
export class StudentComponent implements OnInit {
  upcomingLessons: number = 0;
  completedLessons: number = 0;

  constructor(
    public userService: UserService,
    private httpService: HttpService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {
  }

  ngOnInit() {
    this.loadStatistics();
  }

  loadStatistics() {
    this.httpService.getStudentReservations().subscribe({
      next: (reservations: any[]) => {
        const now = new Date();
        this.upcomingLessons = reservations.filter(r => new Date(r.start.dateTime.value) > now).length;
        this.completedLessons = reservations.filter(r => new Date(r.start.dateTime.value) < now).length;
      },
      error: () => {
        this.snackBar.open('Nie udało się załadować statystyk', 'OK', {
          duration: 3000,
          horizontalPosition: 'end',
          verticalPosition: 'top'
        });
      }
    });
  }

  openEditDialog() {
    const dialogRef = this.dialog.open(EditProfileDialogComponent, {
      width: '400px',
      data: {
        name: this.userService.student?.name,
        surname: this.userService.student?.surname,
        phoneNumber: this.userService.student?.phoneNumber
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        // TODO: Update profile
        // this.httpService.updateStudentProfile(result).subscribe({
        //   next: () => {
        //     this.userService.student = {...this.userService.student, ...result};
        //     this.snackBar.open('Profil został zaktualizowany', 'OK', {
        //       duration: 3000,
        //       horizontalPosition: 'end',
        //       verticalPosition: 'top'
        //     });
        //   },
        //   error: () => {
        //     this.snackBar.open('Nie udało się zaktualizować profilu', 'OK', {
        //       duration: 3000,
        //       horizontalPosition: 'end',
        //       verticalPosition: 'top',
        //       panelClass: ['error-snackbar']
        //     });
        //   }
        // });
      }
    });
  }

  openChangePasswordDialog() {
    // TODO: Implement change password dialog
    this.snackBar.open('Funkcja w trakcie implementacji', 'OK', {
      duration: 2000
    });
  }
}
