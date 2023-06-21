import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  username: string = '';
  email: string = '';
  // Add other attributes as needed
  constructor() { }
}
