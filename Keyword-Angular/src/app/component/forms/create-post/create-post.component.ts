import { UserService } from './../../../service/user.service';
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

  createPostError: boolean = false;
  errorMessage: string = '';


  constructor(private route: ActivatedRoute,
    private router: Router,
    private postService: PostService,
    userService: UserService) {

      if(this.routePostId != null){
        this.postService.get(Number(this.routePostId)).pipe(take(1))
        .subscribe(res => {
          this.title = res.title;
          this.description = res.description;
        });
      }

      userService.getSubscribed().pipe(take(1)).subscribe(res => {
        this.groups = res;
        if(this.routeGroupId!=null) {
          this.group = this.routeGroupId;
        }
      });
  }

  create(){
    if(this.routePostId==null) {
      this.postService.add(this.title, this.description, this.group).subscribe(res => {
        this.routePostId = res.id.toString();
        this.router.navigate(['/' + this.group + '/' + this.routePostId]);
      }, err => {
        this.createPostError = true;
        this.errorMessage = err.error;
      });
    } else {
      this.postService.edit(Number(this.routePostId), this.title, this.description).subscribe(
        () =>{
          this.router.navigate(['/' + this.routeGroupId + '/' + this.routePostId]);
        }, 
        err => {
          this.createPostError = true;
          this.errorMessage = err.error;
      });
    }
  }
}
