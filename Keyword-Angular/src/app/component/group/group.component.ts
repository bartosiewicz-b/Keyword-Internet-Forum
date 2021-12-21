import { PostService } from './../../service/post.service';
import { Group } from './../../model/group';
import { GroupService } from './../../service/group.service';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Post } from 'src/app/model/post';

@Component({
  selector: 'app-group',
  templateUrl: './group.component.html',
  styleUrls: ['./group.component.css']
})
export class GroupComponent implements OnInit {
  group: Group = new Group;

  constructor(private postService: PostService,
    private groupService: GroupService,
    private route: ActivatedRoute) { 

      this.groupService.get(route.snapshot.paramMap.get('groupId') as string)
        .subscribe(res => this.group = res);
    }

  ngOnInit(): void {
  }

  subscribe() {
    this.groupService.subscribe(this.group.id);

    if(this.group.isSubscribed)
      this.group.subscriptions--;
    else
      this.group.subscriptions++;
    
    this.group.isSubscribed = !this.group.isSubscribed;
  }

}
