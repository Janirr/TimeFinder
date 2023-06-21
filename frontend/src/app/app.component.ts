import { Component } from '@angular/core';
import { AuthService } from './auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  providers: [AuthService] // Add AuthService here
})
export class AppComponent {
  title = 'Korepetycje';
  constructor(public authService: AuthService) {}
}
