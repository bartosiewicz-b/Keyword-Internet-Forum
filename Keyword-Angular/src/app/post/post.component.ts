import { PostService } from './../service/post.service';
import { Post } from './../model/post';
import { CommentService } from './../service/comment.service';
import { Component, OnInit } from '@angular/core';
import { Comment } from '../model/comment';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.css']
})
export class PostComponent implements OnInit {
  comments: Comment[] = [];
  post: Post = {} as Post;

  constructor(private postService: PostService,
      private commentService: CommentService,
      private route: ActivatedRoute) { 

    let postId = Number(this.route.snapshot.paramMap.get("postId"));

    this.postService.get(postId).subscribe(res => this.post = res);
    this.comments = this.commentService.getAll(1);
  }

  ngOnInit(): void {
  }

}
