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

  create(title: string, description: string, groupId: string) {
    return this.httpClient.post(this.url + '/create',
      {'title': title, 'description': description, 'groupId': groupId})
      .pipe(take(1));
  }

  getAll(page: number, keyword: string): Observable<Post[]> {
    return this.httpClient.get<Post[]>(this.url + '/get-all',
    {params: {"page": page, "name": keyword}})
      .pipe(map(res => {
        return res as Post[];
      }));
  }

  get(id: number): Observable<Post> {
    return this.httpClient.get<Post>(this.url + '/get',
    {params: {"id": id}})
    .pipe(map(res => {
      return res as Post;
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

  edit(id: number, title: string, description: string) {
    this.httpClient.post(this.url + '/edit',
      {'postId': id, 'title': title, 'description': description})
      .pipe(take(1)).subscribe();
  }

  delete(id: number) {
    this.httpClient.post(this.url + '/delete',
      {'id': id})
      .pipe(take(1)).subscribe();
  }
}
