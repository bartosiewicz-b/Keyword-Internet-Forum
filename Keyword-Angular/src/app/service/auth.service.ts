import { BACKEND_URL } from './../url';
import { MemoryService } from './memory.service';
import { take } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private url = BACKEND_URL + '/auth';

  private username: string | null;
  private email: string | null;

  constructor(private httpClient: HttpClient,
              private memoryService: MemoryService) { 

      this.username = memoryService.getUsername();
      this.email = memoryService.getEmail();
    }

  isLoggedIn(): boolean {
    return this.username!=null && this.email!=null;
  }

  getUsername(): string | null {
    return this.username;
  }

  getEmail(): string | null {
    return this.email;
  }

  getToken(): string | null {
    return this.memoryService.getToken();
  }

  logOut() {
    this.memoryService.clear();
    this.username = null;
    this.email = null;
  }

  login(login: string, password: string) {
    return this.httpClient.post<any>(this.url + '/login', 
    {'login': login, 'password': password})
    .pipe(take(1));
  }

  saveLoginData(data: any) {
    this.memoryService.setToken(data.token);
    this.memoryService.setRefresh(data.refreshToken)
    this.memoryService.setUsername(data.username);
    this.memoryService.setEmail(data.email);

    this.username = data.username;
    this.email = data.email;
  }

  register(email: string, username: string, password: string) {
    this.httpClient.post(this.url + '/register', 
      {'email': email, 'username': username, 'password': password})
      .pipe(take(1))
      .subscribe();
  }

  refreshToken() {
    let refresh = this.memoryService.getRefresh();
    if(refresh == null)
      return;

    this.httpClient.post<any>(this.url + '/refresh-token', refresh)
    .pipe(take(1))
    .subscribe(res => {
      this.memoryService.setToken(res.token);
    });
  }

  validateNewEmail(email: string): Observable<boolean> {
    return this.httpClient.post<boolean>(this.url + '/validate-new-email', 
    {'email': email})
    .pipe(take(1));
  }

  validateNewUsername(username: string): Observable<boolean> {
    return this.httpClient.post<boolean>(this.url + '/validate-new-username', 
    {'username': username})
    .pipe(take(1));
  }

  changeAvatar(newAvatarUrl: string){
    this.httpClient.post<any>(this.url + '/change-avatar', newAvatarUrl)
    .pipe(take(1)).subscribe();
  }

  changeUsername(newUsername: string){
    this.httpClient.post<any>(this.url + '/change-username', newUsername)
    .pipe(take(1)).subscribe(
      res => {
        this.memoryService.setToken(res.token);
        this.memoryService.setRefresh(res.refreshToken)
        this.memoryService.setUsername(newUsername);
        this.username = newUsername;
      });
  }

  changeEmail(newEmail: string, password: string){
    this.httpClient.post<any>(this.url + '/change-email', 
    {'newEmail': newEmail, 'password': password})
    .pipe(take(1)).subscribe(
      res => {
        this.memoryService.setToken(res.token);
        this.memoryService.setRefresh(res.refreshToken)
        this.memoryService.setEmail(newEmail);
        this.email = newEmail;
      });
  }

  changePassword(oldPassword: string, newPassword: string){
    this.httpClient.post<any>(this.url + '/change-password', 
    {'oldPassword': oldPassword, 'newPassword': newPassword})
    .pipe(take(1)).subscribe(
      res => {
        this.memoryService.setToken(res.token);
        this.memoryService.setRefresh(res.refreshToken)
      });
  }
}
