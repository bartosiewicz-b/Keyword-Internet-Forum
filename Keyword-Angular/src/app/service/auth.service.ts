import { take } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private url = 'http://localhost:8080/auth';

  constructor(private httpClient: HttpClient) { }

  refreshToken() {

    let refresh = sessionStorage.getItem('refreshToken');

    if(refresh == null)
      return;

    this.httpClient.get<any>(this.url + '/refresh/token', {headers: {'refresh': refresh}})
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

  login(login: string, password: string) {
      this.httpClient.post<any>(this.url + '/login', 
      {'login': login, 'password': password})
      .pipe(take(1))
      .subscribe(
        res => {
          sessionStorage.setItem('token', res.token);
          sessionStorage.setItem('refreshToken', res.refreshToken)
          sessionStorage.setItem('username', login)
        }
      );
  }

  register(email: string, username: string, password: string) {
    this.httpClient.post(this.url + '/register', 
      {'email': email, 'username': username, 'password': password})
      .pipe(take(1))
      .subscribe();
  }

  validateNewEmail(email: string) {
    return this.httpClient.post(this.url + '/validate-new/email', 
    {'email': email})
    .pipe(take(1));
  }

  validateNewUsername(username: string) {
    return this.httpClient.post(this.url + '/validate-new/username', 
    {'username': username})
    .pipe(take(1));
  }
}
