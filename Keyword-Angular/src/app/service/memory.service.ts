import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class MemoryService {

  constructor() { }

  saveLogin(refresh: string, token: string, username: string, email: string) {
    this.setRefresh(refresh);
    this.setToken(token);
    this.setUsername(username);
    this.setEmail(email);
  }

  clear() {
    localStorage.clear();
  }

  setRefresh(value: string) {
    localStorage.setItem('REFRESH_TOKEN', value)
  }

  setToken(value: string) {
    localStorage.setItem('TOKEN', value)
  }

  setUsername(value: string) {
    localStorage.setItem('USERNAME', value)
  }

  setEmail(value: string) {
    localStorage.setItem('EMAIL', value)
  }

  getRefresh() {
    return localStorage.getItem('REFRESH_TOKEN')
  }

  getToken() {
    return localStorage.getItem('TOKEN')
  }

  getUsername() {
    return localStorage.getItem('USERNAME')
  }

  getEmail() {
    return localStorage.getItem('EMAIL')
  }
}
