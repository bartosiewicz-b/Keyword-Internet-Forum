import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map, take } from 'rxjs/operators';
import { Comment } from '../model/comment';

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  url = 'http://localhost:8080/comment';

  constructor(private httpClient: HttpClient) { }

  getAll(postId: number): Observable<Comment[]> {
    return this.httpClient.get<Comment[]>(this.url + '/get',
      {params: {"postId": postId}})
      .pipe(map(res => {
        return res as Comment[];
      }));
  }

  upvote(commentId: number) {
    this.httpClient.post(this.url + '/upvote', {'commentId': commentId})
    .pipe(take(1))
    .subscribe()
  }

  downvote(commentId: number) {
    this.httpClient.post(this.url + '/downvote', {'commentId': commentId})
    .pipe(take(1))
    .subscribe()
  }
}
