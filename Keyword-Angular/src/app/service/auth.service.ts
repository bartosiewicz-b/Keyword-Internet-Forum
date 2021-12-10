import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private httpClient: HttpClient) { }

  login() {
      this.httpClient.post<any>('http://localhost:8080/auth/login', 
      {"login": "testUser1", "password": "password"}).subscribe(
        res => {
          sessionStorage.setItem('token', res.token);
        }
      );
  }
}
