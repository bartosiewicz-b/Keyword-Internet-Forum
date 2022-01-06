import { AppUser } from './../../model/AppUser';
import { UserService } from './../../service/user.service';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {

  user: AppUser = new AppUser;

  constructor(private userService: UserService,
    private route: ActivatedRoute) { 

      let username: string = String(route.snapshot.paramMap.get("username"));

      userService.get(username).subscribe(res => this.user = res);
    }

  ngOnInit(): void {
  }

}
