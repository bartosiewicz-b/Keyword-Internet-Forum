import { AuthService } from './../../service/auth.service';
import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  emailTaken: boolean = false;
  usernameTaken: boolean = false;

  constructor(private router: Router,
    private authService: AuthService) { }

  ngOnInit(): void {
  }

  validateNewEmail(email: string) {
    this.authService.validateNewEmail(email)
    .subscribe(res => this.emailTaken = !res as boolean);
  }

  validateNewUsername(username: string) {
    this.authService.validateNewUsername(username)
    .subscribe(res => this.usernameTaken = !res as boolean);
  }

  register(data: NgForm) {
    this.authService.register(data.value.email, data.value.username, data.value.password)
    this.router.navigateByUrl('/login');
  }
}
