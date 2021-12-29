import { AuthService } from '../../service/auth.service';
import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  constructor(private router: Router,
    private authService: AuthService) { }

  ngOnInit(): void {
  }

  login(data: NgForm) {
    this.authService.login(data.value.login, data.value.password);
    
    this.router.navigate(['/']);
  }
}
