import { Post } from './../model/post';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class PostService {

  constructor(private httpClient: HttpClient) { }

  getAll(): Post[] {
    let posts: Post[] = [];

    this.httpClient.get<Post[]>('http://localhost:8080/post/get-all',
      {params: {"page": 0}}).subscribe(res => {
      res.forEach(post => {
        posts.push(post);
      });
    });
    return posts;
  }
}