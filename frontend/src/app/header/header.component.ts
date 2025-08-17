import {Component} from '@angular/core';
import {AuthService} from '../auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
  standalone: false
})
export class HeaderComponent {
  constructor(public authService: AuthService) {
  }

  logout() {
    this.authService.logout();
  }
}
