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

    let refresh = this.memoryService.getRefresh();
    if(refresh == null)
      return;

    this.httpClient.get<any>(this.url + '/refresh/token', 
    {headers: {'refresh': refresh}})
    .subscribe(res => {
      this.memoryService.setToken(res.token);
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
          this.memoryService.saveLogin(res.refreshToken, res.token, res.username, res.email);
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

  changeUsername(username: string){
    this.httpClient.post<any>(this.url + '/change/username', 
    {'username': username})
    .pipe(take(1)).subscribe(
      res => {
        this.memoryService.setToken(res.token);
        this.memoryService.setRefresh(res.refreshToken)
      }
    );

    this.memoryService.setUsername(username);
  }

  changeEmail(email: string, password: string){
    this.httpClient.post<any>(this.url + '/change/email', 
    {'newEmail': email, 'password': password})
    .pipe(take(1)).subscribe(
      res => {
        this.memoryService.setToken(res.token);
        this.memoryService.setRefresh(res.refreshToken)
      }
    );

    this.memoryService.setEmail(email);
  }

  changePassword(oldPassword: string, newPassword: string){
    this.httpClient.post<any>(this.url + '/change/password', 
    {'oldPassword': oldPassword, 'newPassword': newPassword})
    .pipe(take(1)).subscribe(
      res => {
        this.memoryService.setToken(res.token);
        this.memoryService.setRefresh(res.refreshToken)
      }
    );
  }
}
