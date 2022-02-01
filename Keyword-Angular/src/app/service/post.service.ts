import { BACKEND_URL } from './../url';
import { Post } from './../model/post';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, take } from 'rxjs/operators';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PostService {

  url = BACKEND_URL + '/post';

  constructor(private httpClient: HttpClient) { }

  add(title: string, description: string, groupId: string): Observable<Post> {
    return this.httpClient.post<Post>(this.url + '/add',
      {'title': title, 'description': description, 'groupId': groupId})
      .pipe(take(1));
  }

  getAll(page: number, groupId: string | any, keyword: string): Observable<Post[]> {
    let pars: any = {'page': page, 'groupId': groupId, 'keyword': keyword};

    return this.httpClient.get<Post[]>(this.url + '/get-all',
    {params: pars})
      .pipe(take(1), map(res => {
        return res;
      }));
  }

  getCount(groupId: string | null, keyword: string | null): Observable<number> {
    let pars: any = {'groupId': groupId, 'keyword': keyword};

    return this.httpClient.get<number>(this.url + '/get-count',
    {params: pars})
      .pipe(take(1), map(res => {
        return res;
      }));
  }

  get(postId: number): Observable<Post> {
    return this.httpClient.get<Post>(this.url + '/get',
    {params: {"postId": postId}})
    .pipe(take(1), map(res => {
      return res;
    }));
  }

  edit(postId: number, title: string, description: string): Observable<Post> {
    return this.httpClient.post<Post>(this.url + '/edit',
      {'postId': postId, 'title': title, 'description': description})
      .pipe(take(1), map(res => {
        return res;
      }));
  }

  delete(id: number) {
    this.httpClient.post(this.url + '/delete', id)
      .pipe(take(1))
      .subscribe();
  }

  upvote(id: number): Observable<number> {
    return this.httpClient.post<number>(this.url + '/upvote', id)
    .pipe(take(1));
  }

  downvote(id: number): Observable<number> {
    return this.httpClient.post<number>(this.url + '/downvote', id)
    .pipe(take(1));
  }
}
