import { PostService } from './../service/post.service';
import { Post } from './../model/post';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  posts: Post[];

  constructor(private postService: PostService) { 
    this.posts = postService.getAll();
  }

  ngOnInit(): void {
  }

}
