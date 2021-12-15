import { Component, OnInit } from '@angular/core';
import { Post } from '../../model/post';
import { PostService } from '../../service/post.service';

@Component({
  selector: 'feed',
  templateUrl: './feed.component.html',
  styleUrls: ['./feed.component.css']
})
export class FeedComponent implements OnInit {

  posts: Post[] = [];

  constructor(private postService: PostService) { 
    postService.getAll(0).subscribe(res => {this.posts = res});
  }

  ngOnInit(): void {
  }

}
