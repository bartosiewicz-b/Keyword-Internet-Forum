import { MemoryService } from './../../service/memory.service';
import { take } from 'rxjs/operators';
import { PostService } from '../../service/post.service';
import { Post } from '../../model/post';
import { CommentService } from '../../service/comment.service';
import { Component, OnInit } from '@angular/core';
import { Comment } from '../../model/comment';
import { ActivatedRoute } from '@angular/router';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.css']
})
export class PostComponent implements OnInit {
  username: string | null;
  comments: Comment[] = [];
  post: Post = new Post;

  constructor(memoryService: MemoryService,
    private postService: PostService,
      private commentService: CommentService,
      private route: ActivatedRoute) { 

    let postId = Number(this.route.snapshot.paramMap.get("postId"));

    this.postService.get(postId).subscribe(res => this.post = res);
    this.commentService.getAll(postId).subscribe(res => this.comments = res);

    this.username = memoryService.getUsername();
  }

  ngOnInit(): void {
  }

  comment(data: NgForm){

    this.commentService.create(data.value.content, this.post.id, null)
    .pipe(take(1))
    .subscribe(res => this.comments.push(res as Comment));
  }

  delete(id: number) {
    this.commentService.delete(id);
    this.comments.forEach((value, index) => {
      if(value.id == id) this.comments.splice(index, 1);
    })
  }
}
