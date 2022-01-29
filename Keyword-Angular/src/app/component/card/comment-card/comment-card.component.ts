import { AuthService } from './../../../service/auth.service';
import { Router } from '@angular/router';
import { MemoryService } from './../../../service/memory.service';
import { take } from 'rxjs/operators';
import { VoteType } from './../../../model/voteType';
import { CommentService } from './../../../service/comment.service';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Comment } from 'src/app/model/comment';
import { faArrowUp, faArrowDown, faTimes, faCommentDots, faEdit, faTrash } from '@fortawesome/free-solid-svg-icons';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'comment-card',
  templateUrl: './comment-card.component.html',
  styleUrls: ['./comment-card.component.css']
})
export class CommentCardComponent{
  @Input('comment') comment: Comment = {} as Comment;
  @Output() deleteId = new EventEmitter<number>();
  @Output() respond = new EventEmitter<Comment>();

  editedContent: string = this.comment.content;

  isUserWriting: boolean = false;
  isUserEditing: boolean = false;

  VoteType = VoteType;
  faArrowUp = faArrowUp;
  faArrowDown = faArrowDown;
  faCommentDots = faCommentDots;
  faTimes = faTimes;
  faEdit = faEdit;
  faTrash = faTrash;

  constructor(public authService: AuthService,
    private router: Router,
    private commentService: CommentService) {}


  upvote() {
    if(!this.authService.isLoggedIn()) {
      this.router.navigateByUrl('login');
      return;
    }

    this.commentService.upvote(this.comment.id).subscribe(res => {
      this.comment.votes = res;
    });

    if(this.comment.userVote == VoteType.UP)
      this.comment.userVote = null;
    else
      this.comment.userVote = VoteType.UP;
  }

  downvote() {
    if(!this.authService.isLoggedIn()) {
      this.router.navigateByUrl('login');
      return;
    }
    
    this.commentService.downvote(this.comment.id).subscribe(res => {
      this.comment.votes = res;
    });

    if(this.comment.userVote == VoteType.DOWN)
      this.comment.userVote = null;
    else
      this.comment.userVote = VoteType.DOWN;
  }

  respondComment(data: NgForm) {
    this.commentService.add(data.value.content, this.comment.postId, this.comment.id)
    .subscribe(res => this.respond.emit(res));

    this.isUserWriting = false;
  }

  deleteComment(){
    this.deleteId.emit(this.comment.id);
  }

  initlializeEdit(){
    this.isUserEditing=true; 
    this.editedContent=this.comment.content
  }

  edit(){
    this.isUserEditing = false;
    this.comment.content = this.editedContent;
    this.commentService.edit(this.comment.id, this.comment.content);
  }

}
