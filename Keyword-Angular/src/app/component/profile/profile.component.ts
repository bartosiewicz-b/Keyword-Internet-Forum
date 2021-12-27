import { AuthService } from './../../service/auth.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  editingUsername: boolean = false;
  editingEmail: boolean = false;
  editingPassword: boolean = false;

  newUsernameTaken: boolean = false;
  newEmailTaken: boolean = false;

  sessionStorage = sessionStorage;

  currentUsername: string | null = sessionStorage.getItem('username');
  currentEmail: string | null = sessionStorage.getItem('email');

  constructor(private authService: AuthService) { }

  ngOnInit(): void {
  }

  validateNewEmail(email: string) {
    this.authService.validateNewEmail(email)
    .subscribe(res => this.newEmailTaken = !res as boolean);
  }

  validateNewUsername(username: string) {
    this.authService.validateNewUsername(username)
    .subscribe(res => this.newUsernameTaken = !res as boolean);
  }

  changeUsername(username: string) {
    this.authService.changeUsername(username);

    this.editingUsername = false;
    this.currentUsername = username;
  }

  changeEmail(email: string, password: string) {
    this.authService.changeEmail(email, password);

    this.editingEmail = false;
    this.currentEmail = email;
  }

  changePassword(oldPassword: string, newPassword: string) {
    this.authService.changePassword(oldPassword, newPassword);

    this.editingPassword = false;
  }
}
