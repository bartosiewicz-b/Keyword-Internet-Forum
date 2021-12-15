import { Post } from './../model/post';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map } from 'rxjs/operators';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PostService {

  url = 'http://localhost:8080/post';

  constructor(private httpClient: HttpClient) { }

  getAll(page: number): Observable<Post[]> {
    return this.httpClient.get<Post[]>(this.url + '/get-all',
    {params: {"page": page}})
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
}
