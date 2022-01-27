import { AuthService } from './../../../service/auth.service';
import { Router } from '@angular/router';
import { MemoryService } from './../../../service/memory.service';
import { take } from 'rxjs/operators';
import { VoteType } from './../../../model/voteType';
import { CommentService } from './../../../service/comment.service';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Comment } from 'src/app/model/comment';
import { faArrowUp, faArrowDown, faTimes } from '@fortawesome/free-solid-svg-icons';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'comment-card',
  templateUrl: './comment-card.component.html',
  styleUrls: ['./comment-card.component.css']
})
export class CommentCardComponent{
  @Input('comment') comment: Comment = {} as Comment;
  @Output() deleteId = new EventEmitter<number>();

  editedContent: string = this.comment.content;

  isUserWriting: boolean = false;
  isUserEditing: boolean = false;
  username: string | null = this.authService.getUsername();

  VoteType = VoteType;
  faArrowUp = faArrowUp;
  faArrowDown = faArrowDown;
  faTimes = faTimes;

  constructor(private authService: AuthService,
    private router: Router,
    private commentService: CommentService) {}


  upvote() {
    if(!this.authService.isLoggedIn()) {
      this.router.navigateByUrl('login');
      return;
    }

    this.commentService.upvote(this.comment.id);

    if(this.comment.userVote == null) {
      this.comment.votes++;
      this.comment.userVote = VoteType.UP;

    } else if(this.comment.userVote == VoteType.UP) {
      this.comment.votes--;
      this.comment.userVote = null;
    } else {
      this.comment.votes+=2;
      this.comment.userVote = VoteType.UP;
    }
  }

  downvote() {
    if(!this.authService.isLoggedIn()) {
      this.router.navigateByUrl('login');
      return;
    }
    
    this.commentService.downvote(this.comment.id);

    if(this.comment.userVote == null) {
      this.comment.votes--;
      this.comment.userVote = VoteType.DOWN;

    } else if(this.comment.userVote == VoteType.DOWN) {
      this.comment.votes++;
      this.comment.userVote = null;
    } else {
      this.comment.votes-=2;
      this.comment.userVote = VoteType.DOWN;
    }
  }

  respondComment(data: NgForm) {
    this.commentService.add(data.value.content, this.comment.postId, this.comment.id)
    .subscribe();

    this.isUserWriting = false;
  }

  deleteComment(){
    this.deleteId.emit(this.comment.id);
  }

  editComment(){
    this.isUserEditing = false;
    this.comment.content = this.editedContent;
    this.commentService.edit(this.comment.id, this.comment.content);
  }

}
