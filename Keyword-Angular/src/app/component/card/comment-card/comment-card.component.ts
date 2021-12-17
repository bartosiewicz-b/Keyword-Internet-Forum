import { VoteType } from './../../../model/voteType';
import { CommentService } from './../../../service/comment.service';
import { Component, Input } from '@angular/core';
import { Comment } from 'src/app/model/comment';
import { faArrowUp, faArrowDown } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'comment-card',
  templateUrl: './comment-card.component.html',
  styleUrls: ['./comment-card.component.css']
})
export class CommentCardComponent{
  @Input('comment') comment: Comment = {} as Comment;

  VoteType = VoteType;
  faArrowUp = faArrowUp;
  faArrowDown = faArrowDown;

  constructor(private commentService: CommentService) {}


  upvote() {
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

}
