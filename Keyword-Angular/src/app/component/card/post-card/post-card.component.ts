import { AuthService } from './../../../service/auth.service';
import { Router } from '@angular/router';
import { VoteType } from './../../../model/voteType';
import { PostService } from './../../../service/post.service';
import { Post } from '../../../model/post';
import { Component, Input } from '@angular/core';
import { faArrowDown, faArrowUp } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'post-card',
  templateUrl: './post-card.component.html',
  styleUrls: ['./post-card.component.css']
})
export class PostCardComponent {
  @Input('post') post: Post = new Post;
  @Input('short') short: boolean = false;

  VoteType = VoteType;
  faArrowUp = faArrowUp;
  faArrowDown = faArrowDown;

  constructor(public authService: AuthService,
    private router: Router,
    private postService: PostService) { 
  }

  upvote() {
    if(!this.authService.isLoggedIn()) {
      this.router.navigateByUrl('login');
      return;
    }

    this.postService.upvote(this.post.id).subscribe(res => {
      this.post.votes = res;
    });
    
    if(this.post.userVote == VoteType.UP)
      this.post.userVote = null;
    else
      this.post.userVote = VoteType.UP;
  }

  downvote() {
    if(!this.authService.isLoggedIn()) {
      this.router.navigateByUrl('login');
      return;
    }

    this.postService.downvote(this.post.id).subscribe(res => {
      this.post.votes = res;
    })

    if(this.post.userVote == VoteType.DOWN)
      this.post.userVote = null;
    else
      this.post.userVote = VoteType.DOWN;
  }

  delete() {
    this.postService.delete(this.post.id);
    this.router.navigate(['/']);
  }
}
