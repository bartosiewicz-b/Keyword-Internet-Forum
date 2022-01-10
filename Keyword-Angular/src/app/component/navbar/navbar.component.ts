import { MemoryService } from './../../service/memory.service';
import { Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

  constructor(public memoryService: MemoryService,
    private router: Router) { 
  }

  ngOnInit(): void {
  }

  logout() {
    this.memoryService.clear();
    this.router.navigate(['/']);
    window.location.reload();
  }

}
