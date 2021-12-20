import { take } from 'rxjs/operators';
import { Component, OnInit } from '@angular/core';
import { Post } from '../../model/post';
import { PostService } from '../../service/post.service';

@Component({
  selector: 'feed',
  templateUrl: './feed.component.html',
  styleUrls: ['./feed.component.css']
})
export class FeedComponent implements OnInit {

  prevValue: string = '';
  posts: Post[] = [];

  constructor(private postService: PostService) { 
    postService.getAll(0, '').pipe(take(1),).subscribe(res => {this.posts = res});
  }

  ngOnInit(): void {
  }

  search(value: string) {
    if(value != this.prevValue){
      if(value.length > 3) 
        this.postService.getAll(0, value).pipe(take(1),).subscribe(res => {this.posts = res});
      else if(this.prevValue.length > 3)
        this.postService.getAll(0, '').pipe(take(1),).subscribe(res => {this.posts = res});

      this.prevValue = value;
    }
    
  }
}
