import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class HttpService {
  private username = 'janir';
  private password = 'root';
  private credentials = btoa(this.username + ':' + this.password); // Encode the username and password
  private urlTemplate = 'http://timefind.eu-central-1.elasticbeanstalk.com';
  private httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': 'Basic ' + this.credentials // Add the Basic Authentication header
    })
  };

  constructor(private http: HttpClient) {}

  get(url: string) {
    return this.http.get(this.urlTemplate + url, this.httpOptions);
  }

  post(url: string, data: any) {
    return this.http.post(this.urlTemplate + url, data, this.httpOptions);
  }
}
