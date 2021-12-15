import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Comment } from '../model/comment';

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  constructor(private httpClient: HttpClient) { }

  getAll(postId: number): Comment[] {
    let comments: Comment[] = [];

    this.httpClient.get<Comment[]>("http://localhost:8080/comment/get",
      {params: {"postId": postId}}).subscribe(res => {
        res.forEach(comment => {
          comments.push(comment);
        })
      });

    return comments;
  }
}
