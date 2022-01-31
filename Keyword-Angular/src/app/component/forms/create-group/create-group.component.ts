import { take } from 'rxjs/operators';
import { ActivatedRoute, Router } from '@angular/router';
import { GroupService } from './../../../service/group.service';
import { Component } from '@angular/core';

@Component({
  selector: 'app-create-group',
  templateUrl: './create-group.component.html',
  styleUrls: ['./create-group.component.css']
})
export class CreateGroupComponent {
  groupId = this.route.snapshot.paramMap.get('groupId');

  groupName: string = '';
  description: string = '';
  avatarUrl: string = '';

  createGroupError: boolean = false;

  constructor(private route: ActivatedRoute,
    private router: Router,
    private groupService: GroupService) { 

      if(this.groupId != null)
        this.groupService.get(this.groupId).pipe(take(1)).subscribe(res => {
          this.groupName = res.groupName;
          this.description = res.description;
          this.avatarUrl = res.avatarUrl
        });
    }

  create(){
    if(this.groupId == null) {
      this.groupService.add(this.groupName, this.description).subscribe(() => {},
      () => {
        this.createGroupError = true;
      });
      this.router.navigate(['/']);
    } else {
      this.groupService.edit(this.groupId, this.groupName, this.description, this.avatarUrl).subscribe(() => {},
      () => {
        this.createGroupError = true;
      });
      this.router.navigate(['/']);
    }
    
  }
}
