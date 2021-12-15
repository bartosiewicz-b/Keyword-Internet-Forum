import { Component, Input, OnInit } from '@angular/core';
import { Comment } from 'src/app/model/comment';

@Component({
  selector: 'comment-card',
  templateUrl: './comment-card.component.html',
  styleUrls: ['./comment-card.component.css']
})
export class CommentCardComponent implements OnInit {
  @Input('comment') comment: Comment = {} as Comment;

  constructor() { }

  ngOnInit(): void {
  }

}
