import { Router } from '@angular/router';
import { GroupService } from './../../../service/group.service';
import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-create-group',
  templateUrl: './create-group.component.html',
  styleUrls: ['./create-group.component.css']
})
export class CreateGroupComponent implements OnInit {

  constructor(private router: Router,
    private groupService: GroupService) { }

  ngOnInit(): void {
  }

  create(data: NgForm){
    this.groupService.createGroup(data.value.groupName, data.value.description);
    this.router.navigate(['/']);
  }
}
