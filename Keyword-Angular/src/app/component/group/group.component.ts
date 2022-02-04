import { MemoryService } from './../../service/memory.service';
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

  deleting: boolean = false;

  isError: boolean = false;
  isLoading: boolean = true;

  constructor(private memoryService: MemoryService,
    private groupService: GroupService,
    private router: Router,
    route: ActivatedRoute) { 

      this.groupService.get(route.snapshot.paramMap.get('groupId') as string)
        .subscribe(res => {
            this.group = res;
            this.isLoading = false;
          },
          err => {
            this.isError = true;
            this.isLoading = false;
          });
    }

  subscribe() {
    if(this.group==null)
      return;

    this.groupService.subscribe(this.group.id);

    if(this.group.isSubscribed)
      this.group.subscriptions--;
    else
      this.group.subscriptions++;
    
    this.group.isSubscribed = !this.group.isSubscribed;
  }

  delete(event: any) {
    if(this.group==null || event==false) {
      this.deleting = false;
      return;
    }
      
    this.groupService.delete(this.group.id);
    this.router.navigateByUrl('/');
  }

}
