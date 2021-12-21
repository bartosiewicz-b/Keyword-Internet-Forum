import { GroupService } from './../../../service/group.service';
import { Group } from './../../../model/group';
import { Component, OnInit } from '@angular/core';
import { take } from 'rxjs/operators';

@Component({
  selector: 'group-feed',
  templateUrl: './group-feed.component.html',
  styleUrls: ['./group-feed.component.css']
})
export class GroupFeedComponent implements OnInit {

  prevValue: string = '';
  groups: Group[] = [];

  constructor(private groupService: GroupService) { 
    this.groupService.getAll(0, '').pipe(take(1),).subscribe(res => {this.groups = res});
  }

  ngOnInit(): void {
  }

  search(value: string) {
    if(value != this.prevValue){
      if(value.length > 3) 
        this.groupService.getAll(0, value).pipe(take(1),).subscribe(res => {this.groups = res});
      else if(this.prevValue.length > 3)
        this.groupService.getAll(0, '').pipe(take(1),).subscribe(res => {this.groups = res});

      this.prevValue = value;
    }
    
  }
}
