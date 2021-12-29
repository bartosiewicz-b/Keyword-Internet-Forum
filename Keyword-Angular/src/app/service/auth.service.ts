import { MemoryService } from './memory.service';
import { take } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private url = 'http://localhost:8080/auth';

  constructor(private httpClient: HttpClient,
      private memoryService: MemoryService) { }

  refreshToken() {

    let refresh = this.memoryService.getRefreshToken();

    if(refresh == null)
      return;

    this.httpClient.get<any>(this.url + '/refresh/token', {headers: {'refresh': refresh}})
    .subscribe(res => {
      this.memoryService.setToken(res.token);
      });
  }

  getToken() {
    return this.memoryService.getToken();
  }

  getRefresh() {
    return this.memoryService.getRefreshToken();
  }

  login(login: string, password: string) {
      this.httpClient.post<any>(this.url + '/login', 
      {'login': login, 'password': password})
      .pipe(take(1))
      .subscribe(
        res => {
          this.memoryService.saveLoginData(res.token, res.refreshToken, res.username, res.email);
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
    {'name': username})
    .pipe(take(1));
  }

  changeUsername(username: string){
    this.httpClient.post<any>(this.url + '/change/username', 
    {'name': username})
    .pipe(take(1)).subscribe(
      res => {
        this.memoryService.setToken(res.token);
        this.memoryService.setRefreshToken(res.refreshToken);
      }
    );

    sessionStorage.setItem('username', username);
  }

  changeEmail(email: string, password: string){
    this.httpClient.post<any>(this.url + '/change/email', 
    {'newEmail': email, 'password': password})
    .pipe(take(1)).subscribe(
      res => {
        this.memoryService.setToken(res.token);
        this.memoryService.setRefreshToken(res.refreshToken);
      }
    );

    sessionStorage.setItem('email', email);
  }

  changePassword(oldPassword: string, newPassword: string){
    this.httpClient.post<any>(this.url + '/change/password', 
    {'oldPassword': oldPassword, 'newPassword': newPassword})
    .pipe(take(1)).subscribe(
      res => {
        this.memoryService.setToken(res.token);
        this.memoryService.setRefreshToken(res.refreshToken);
      }
    );
  }
}
