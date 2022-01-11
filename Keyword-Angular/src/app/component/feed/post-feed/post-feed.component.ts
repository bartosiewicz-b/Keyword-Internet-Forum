import { take } from 'rxjs/operators';
import { Component, Input, OnInit } from '@angular/core';
import { Post } from '../../../model/post';
import { PostService } from '../../../service/post.service';

@Component({
  selector: 'post-feed',
  templateUrl: './post-feed.component.html',
  styleUrls: ['./post-feed.component.css']
})
export class PostFeedComponent implements OnInit {
  @Input('groupId') groupId: string | null = null;

  prevValue: string = '';
  posts: Post[] = [];

  constructor(private postService: PostService) {
  }

  ngOnInit(): void {
    this.postService.getAll(0, this.groupId, '').pipe(take(1),)
    .subscribe(res => this.posts = res);
  }

  search(value: string) {
    if(value != this.prevValue){
      if(value.length > 3) 
        this.postService.getAll(0, this.groupId, value).pipe(take(1),).subscribe(res => {this.posts = res});
      else if(this.prevValue.length > 3)
        this.postService.getAll(0, this.groupId, '').pipe(take(1),).subscribe(res => {this.posts = res});

      this.prevValue = value;
    }
    
  }
}
