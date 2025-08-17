import {Component, Inject} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {MAT_DIALOG_DATA, MatDialogModule, MatDialogRef} from '@angular/material/dialog';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatButtonModule} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-edit-profile-dialog',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatDialogModule
  ],
  template: `
    <h2 mat-dialog-title>Edytuj profil</h2>
    <mat-dialog-content>
      <form [formGroup]="profileForm" class="profile-form">
        <mat-form-field appearance="outline" class="full-width">
          <mat-label>Imię</mat-label>
          <input matInput formControlName="name" required>
          <mat-error *ngIf="profileForm.get('name')?.hasError('required')">
            Imię jest wymagane
          </mat-error>
        </mat-form-field>

        <mat-form-field appearance="outline" class="full-width">
          <mat-label>Nazwisko</mat-label>
          <input matInput formControlName="surname" required>
          <mat-error *ngIf="profileForm.get('surname')?.hasError('required')">
            Nazwisko jest wymagane
          </mat-error>
        </mat-form-field>

        <mat-form-field appearance="outline" class="full-width">
          <mat-label>Telefon</mat-label>
          <input matInput formControlName="phoneNumber" required>
          <mat-icon matSuffix>phone</mat-icon>
          <mat-error *ngIf="profileForm.get('phoneNumber')?.hasError('required')">
            Numer telefonu jest wymagany
          </mat-error>
          <mat-error *ngIf="profileForm.get('phoneNumber')?.hasError('pattern')">
            Wprowadź poprawny numer telefonu
          </mat-error>
        </mat-form-field>
      </form>
    </mat-dialog-content>
    <mat-dialog-actions align="end">
      <button mat-button (click)="onCancel()">Anuluj</button>
      <button mat-raised-button color="primary"
              [disabled]="profileForm.invalid"
              (click)="onSubmit()">
        Zapisz zmiany
      </button>
    </mat-dialog-actions>
  `,
  styles: [`
    .profile-form {
      display: flex;
      flex-direction: column;
      gap: 16px;
      min-width: 300px;
      padding: 16px 0;
    }

    .full-width {
      width: 100%;
    }

    mat-dialog-actions {
      margin-top: 16px;
      gap: 8px;
    }
  `]
})
export class EditProfileDialogComponent {
  profileForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<EditProfileDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    this.profileForm = this.fb.group({
      name: [data.name, Validators.required],
      surname: [data.surname, Validators.required],
      phoneNumber: [data.phoneNumber, [Validators.required, Validators.pattern('^[0-9]{9}$')]]
    });
  }

  onCancel(): void {
    this.dialogRef.close();
  }

  onSubmit(): void {
    if (this.profileForm.valid) {
      this.dialogRef.close(this.profileForm.value);
    }
  }
}
