import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

  username: string | null = null;

  constructor() { 
    this.username = sessionStorage.getItem('username');
  }

  ngOnInit(): void {
  }

}
