import { Router } from '@angular/router';
import { AuthService } from './../../service/auth.service';
import { Component } from '@angular/core';

@Component({
  selector: 'navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {

  constructor(public authService: AuthService,
    private router: Router) { }

  logout() {
    this.authService.logOut();
    this.router.navigateByUrl('/');
  }
}
