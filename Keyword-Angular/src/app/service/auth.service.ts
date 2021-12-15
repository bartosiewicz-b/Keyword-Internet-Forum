import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private httpClient: HttpClient) { }

  refreshToken() {

    let refresh = sessionStorage.getItem('refreshToken');

    if(refresh == null)
      return;

    this.httpClient.get<any>('http://localhost:8080/auth/refresh/token', {headers: {'refresh': refresh}})
    .subscribe(res => {
        sessionStorage.setItem('token', res.token);
      });
  }

  getToken() {
    return localStorage.getItem('token');
  }

  getRefresh() {
    return localStorage.getItem('refreshToken');
  }

  login() {
      this.httpClient.post<any>('http://localhost:8080/auth/login', 
      {"login": "testUser1", "password": "password"}).subscribe(
        res => {
          sessionStorage.setItem('token', res.token);
          sessionStorage.setItem('refreshToken', res.refreshToken)
        }
      );
  }
}
