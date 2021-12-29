import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class MemoryService {

  constructor() { }

  saveLoginData(token: string, refreshToken: string, username: string, email: string) {
    localStorage.setItem('token', token);
    localStorage.setItem('refreshToken', refreshToken);
    localStorage.setItem('username', username);
    localStorage.setItem('email', email);
  }

  deleteLoginData() {
    localStorage.clear();
  }

  getUsername(): string | null {
    return localStorage.getItem('username');
  }

  getEmail(): string | null {
    return localStorage.getItem('email');
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  getRefreshToken(): string | null {
    return localStorage.getItem('refreshToken');
  }

  setUsername(username: string) {
    localStorage.setItem('username', username);
  }

  setEmail(email: string) {
    localStorage.setItem('email', email);
  }

  setToken(token: string) {
    localStorage.setItem('token', token);
  }

  setRefreshToken(refreshToken: string) {
    localStorage.setItem('refreshToken', refreshToken);
  }
}
