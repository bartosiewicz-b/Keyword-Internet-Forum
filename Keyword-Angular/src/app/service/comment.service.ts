import { BACKEND_URL } from './../url';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map, take } from 'rxjs/operators';
import { Comment } from '../model/comment';

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  private url = BACKEND_URL + '/comment';

  constructor(private httpClient: HttpClient) { }

  add(content: string, postId: number, parentCommentId: number | null): Observable<Comment>{
    return this.httpClient.post<Comment>(this.url + '/add',
    {'content': content, 'postId': postId, 'parentCommentId': parentCommentId})
    .pipe(take(1));
  }

  getAll(postId: number): Observable<Comment[]> {
    return this.httpClient.get<Comment[]>(this.url + '/get-all',
      {params: {"postId": postId}})
      .pipe(take(1), map(res => {
        return res as Comment[];
      }));
  }

  edit(id: number, newContent: string) {
    this.httpClient.post(this.url + '/edit',
    {'id': id, 'newContent': newContent})
    .pipe(take(1))
    .subscribe();
  }

  delete(id: number){
    this.httpClient.post(this.url + '/delete', id)
    .pipe(take(1))
    .subscribe();
  }

  upvote(id: number) {
    this.httpClient.post(this.url + '/upvote', id)
    .pipe(take(1))
    .subscribe()
  }

  downvote(id: number) {
    this.httpClient.post(this.url + '/downvote', id)
    .pipe(take(1))
    .subscribe()
  }
}
