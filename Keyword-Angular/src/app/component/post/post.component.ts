import { AuthService } from './../../service/auth.service';
import { GroupService } from './../../service/group.service';
import { Group } from './../../model/group';
import { PostService } from '../../service/post.service';
import { Post } from '../../model/post';
import { CommentService } from '../../service/comment.service';
import { Component, OnInit } from '@angular/core';
import { Comment } from '../../model/comment';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.css']
})
export class PostComponent implements OnInit {
  comments: Comment[] = [];
  post: Post = new Post;
  group: Group = new Group;
  isModerator: boolean = false;

  isError: boolean = false;
  isLoading: boolean = true;

  newComment: string = '';
  createCommentError: boolean = false;
  errorMessage: string = '';

  constructor(public authService: AuthService,
    private groupService: GroupService,
    private postService: PostService,
      private commentService: CommentService,
      private route: ActivatedRoute,
      private router: Router) { 

    let postId = Number(this.route.snapshot.paramMap.get("postId"));
    let groupId = String(this.route.snapshot.paramMap.get("groupId"));

    this.postService.get(postId).subscribe(res => {
      this.post = res;
      this.isLoading = false;
    }, err => {
      this.isError = true;
      this.isLoading = false;
    });
    this.commentService.getAll(postId).subscribe(res => {
      this.comments = res
      this.isLoading = false;
    }, err => {
      this.isError = true;
      this.isLoading = false;
    });
    this.groupService.get(groupId).subscribe(res => {
      this.group = res;
      this.isModerator = this.group.moderators.includes(String(authService.getUsername()));
      this.isLoading = false;
    }, err => {
      this.isError = true;
      this.isLoading = false;
    });
  }

  ngOnInit(): void {
  }

  comment(){

    this.commentService.add(this.newComment, this.post.id, null)
    .subscribe(res => {
      this.comments.push(res)
      this.createCommentError = false;
      this.newComment = '';
    },
    err => {
      this.createCommentError = true;
      this.errorMessage = err.error;
    });
  }

  deleteComment(id: number) {
    this.commentService.delete(id);
    this.comments.forEach((value, index) => {
      if(value.id == id) this.comments.splice(index, 1);
    })
  }

  deletePost() {
    this.postService.delete(this.post.id);
    this.router.navigateByUrl('/' + this.group.id);
  }

  respondComment(comment: Comment) {
    this.comments.push(comment);
  }
}
