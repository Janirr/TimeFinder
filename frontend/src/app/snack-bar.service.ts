import {Injectable} from '@angular/core';
import {MatSnackBar} from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root',  // Provides the service globally
})
export class SnackBarService {
  constructor(private snackBar: MatSnackBar) {
  }

  open(message: string, action: string = 'Close', duration: number = 5000, panelClass: string[] = ['default-snackbar']) {
    this.snackBar.open(message, action, {
      duration: duration,
      verticalPosition: 'top',  // Ensure it's always at the top
      horizontalPosition: 'center',
      panelClass: panelClass,  // Allows custom styling
    });
  }

  showError(message: string) {
    this.open(message, 'Close', 5000, ['error-snackbar']);
  }

  showSuccess(message: string) {
    this.open(message, 'Close', 5000, ['success-snackbar']);
  }
}
