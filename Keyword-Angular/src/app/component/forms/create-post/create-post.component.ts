import { GroupService } from './../../../service/group.service';
import { Group } from './../../../model/group';
import { take } from 'rxjs/operators';
import { PostService } from './../../../service/post.service';
import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-create-post',
  templateUrl: './create-post.component.html',
  styleUrls: ['./create-post.component.css']
})
export class CreatePostComponent {
  groups: Group[] = [];

  routePostId = this.route.snapshot.paramMap.get('postId');
  routeGroupId = this.route.snapshot.paramMap.get('groupId');

  title: string = '';
  description: string = '';
  group: string = '';


  constructor(private route: ActivatedRoute,
    private router: Router,
    private postService: PostService,
    groupService: GroupService) {

      if(this.routePostId != null){
        this.postService.get(Number(this.routePostId)).pipe(take(1))
        .subscribe(res => {
          this.title = res.title;
          this.description = res.description;
        });
      }

      groupService.getSubscribed().pipe(take(1)).subscribe(res => {
        this.groups = res;
        if(this.routeGroupId!=null) {
          this.group = this.routeGroupId;
        }
      });
  }

  create(){
    if(this.routePostId==null) {
      this.postService.create(this.title, this.description, this.group).subscribe(res => {
        this.routePostId = res as string;
        this.router.navigate(['/' + this.group + '/' + this.routePostId]);
      });
    } else {
      this.postService.edit(Number(this.routePostId), this.title, this.description).subscribe();
      this.router.navigate(['/' + this.routeGroupId + '/' + this.routePostId]);
    }
  }
}
