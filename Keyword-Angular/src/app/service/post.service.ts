import { Post } from './../model/post';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, take } from 'rxjs/operators';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PostService {

  url = 'http://localhost:8080/post';

  constructor(private httpClient: HttpClient) { }

  getAll(page: number, keyword: string): Observable<Post[]> {
    return this.httpClient.get<Post[]>(this.url + '/get-all',
    {params: {"page": page, "name": keyword}})
      .pipe(map(res => {
        return res as Post[];
      }));
  }

  get(postId: number): Observable<Post> {
    return this.httpClient.get<Post>(this.url + '/get',
    {params: {"id": postId}})
    .pipe(map(res => {
      return res as Post;
    }));
  }

  upvote(postId: number) {
    this.httpClient.post(this.url + '/upvote', {'postId': postId})
    .pipe(take(1))
    .subscribe()
  }

  downvote(postId: number) {
    this.httpClient.post(this.url + '/downvote', {'postId': postId})
    .pipe(take(1))
    .subscribe()
  }
}
