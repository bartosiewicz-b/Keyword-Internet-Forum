import { VoteType } from './../../../model/voteType';
import { PostService } from './../../../service/post.service';
import { Post } from '../../../model/post';
import { Component, Input, OnInit } from '@angular/core';
import { faArrowDown, faArrowUp } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'post-card',
  templateUrl: './post-card.component.html',
  styleUrls: ['./post-card.component.css']
})
export class PostCardComponent implements OnInit {
  @Input('post') post: Post = {} as Post;
  @Input('excerpt') excerpt: boolean = false;
  @Input('enableVoting') enableVoting: boolean = true;

  VoteType = VoteType;
  faArrowUp = faArrowUp;
  faArrowDown = faArrowDown;

  constructor(private PostService: PostService) { 
  }

  ngOnInit(): void {
  }

  upvote() {
    if(!this.enableVoting)
      return;

    this.PostService.upvote(this.post.id);

    if(this.post.userVote == null) {
      this.post.votes++;
      this.post.userVote = VoteType.UP;

    } else if(this.post.userVote == VoteType.UP) {
      this.post.votes--;
      this.post.userVote = null;
    } else {
      this.post.votes+=2;
      this.post.userVote = VoteType.UP;
    }
  }

  downvote() {
    if(!this.enableVoting)
      return;

    this.PostService.downvote(this.post.id);

    if(this.post.userVote == null) {
      this.post.votes--;
      this.post.userVote = VoteType.DOWN;

    } else if(this.post.userVote == VoteType.DOWN) {
      this.post.votes++;
      this.post.userVote = null;
    } else {
      this.post.votes-=2;
      this.post.userVote = VoteType.DOWN;
    }
  }

}
