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

  create(content: string, postId: number, parentCommentId: number | null){
    return this.httpClient.post(this.url + '/create',
    {'content': content, 'postId': postId, 'parentCommentId': parentCommentId})
    .pipe(map(res => {
      return res as Comment;
    }));
  }

  getAll(postId: number): Observable<Comment[]> {
    return this.httpClient.get<Comment[]>(this.url + '/get',
      {params: {"postId": postId}})
      .pipe(map(res => {
        return res as Comment[];
      }));
  }

  upvote(id: number) {
    this.httpClient.post(this.url + '/upvote', {'id': id})
    .pipe(take(1))
    .subscribe()
  }

  downvote(id: number) {
    this.httpClient.post(this.url + '/downvote', {'id': id})
    .pipe(take(1))
    .subscribe()
  }

  edit(id: number, newContent: string) {
    this.httpClient.post(this.url + '/edit',
    {'id': id, 'newContent': newContent})
    .pipe(take(1))
    .subscribe();
  }

  delete(id: number){
    this.httpClient.post(this.url + '/delete',
    {'id': id})
    .pipe(take(1))
    .subscribe();
  }
}
