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
  postId = this.route.snapshot.paramMap.get('postId');
  groupId = this.route.snapshot.paramMap.get('groupId');

  title: string = '';
  description: string = '';


  constructor(private route: ActivatedRoute,
    private router: Router,
    private postService: PostService) {

      if(this.postId != null){
        this.postService.get(Number(this.postId)).pipe(take(1))
        .subscribe(res => {
          this.title = res.title;
          this.description = res.description;
        });
      }
  }

  create(){
    if(this.groupId != null) {
      if(this.postId==null) {
        this.postService.create(this.title, this.description, this.groupId).subscribe(res => {
          this.postId = res as string;
          this.router.navigate(['/' + this.groupId + '/' + this.postId]);
        });
      }
      else {
        this.postService.edit(Number(this.postId), this.title, this.description).subscribe();
        this.router.navigate(['/' + this.groupId + '/' + this.postId]);
      }
    }
  }
}
