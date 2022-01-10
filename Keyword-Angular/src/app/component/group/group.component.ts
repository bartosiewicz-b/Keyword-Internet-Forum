import { MemoryService } from './../../service/memory.service';
import { PostService } from './../../service/post.service';
import { Group } from './../../model/group';
import { GroupService } from './../../service/group.service';
import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-group',
  templateUrl: './group.component.html',
  styleUrls: ['./group.component.css']
})
export class GroupComponent {
  group: Group = new Group;

  username: string | null = this.memoryService.getUsername();

  constructor(private memoryService: MemoryService,
    private postService: PostService,
    private groupService: GroupService,
    private router: Router,
    private route: ActivatedRoute) { 

      this.groupService.get(route.snapshot.paramMap.get('groupId') as string)
        .subscribe(res => this.group = res);
    }

  subscribe() {
    this.groupService.subscribe(this.group.id);

    if(this.group.isSubscribed)
      this.group.subscriptions--;
    else
      this.group.subscriptions++;
    
    this.group.isSubscribed = !this.group.isSubscribed;
  }

  delete() {
    this.groupService.deleteGroup(this.group.id);
    this.router.navigate(['/']);
  }

}
