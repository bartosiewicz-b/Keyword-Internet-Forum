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

  getAll(page: number, groupId: string | any, keyword: string): Observable<Post[]> {
    let pars: any;

    if(groupId==null)
      pars = {"page": page, "name": keyword};
    else
      pars = {"page": page, "groupId": groupId, "name": keyword};

    return this.httpClient.get<Post[]>(this.url + '/get-all',
    {params: pars})
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

  create(title: string, description: string, groupId: string) {
    return this.httpClient.post(this.url + '/create',
      {'title': title, 'description': description, 'groupId': groupId})
      .pipe(take(1));
  }

  edit(postId: number, title: string, description: string) {
    return this.httpClient.post(this.url + '/edit',
      {'postId': postId, 'title': title, 'description': description})
      .pipe(take(1));
  }

  delete(postId: number) {
    return this.httpClient.post(this.url + '/delete',
      {'id': postId})
      .pipe(take(1)).subscribe();
  }
}
