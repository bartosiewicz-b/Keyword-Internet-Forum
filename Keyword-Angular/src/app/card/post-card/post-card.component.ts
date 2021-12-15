import { Post } from './../../model/post';
import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'post-card',
  templateUrl: './post-card.component.html',
  styleUrls: ['./post-card.component.css']
})
export class PostCardComponent implements OnInit {
  @Input('post') post: Post = {} as Post;
  @Input('excerpt') excerpt: boolean = false;

  constructor() { 
  }

  ngOnInit(): void {
  }

}
