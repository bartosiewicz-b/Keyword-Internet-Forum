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
      pars = {"page": page, "keyword": keyword};
    else
      pars = {"page": page, "groupId": groupId, "keyword": keyword};

    return this.httpClient.get<Post[]>(this.url + '/get-all',
    {params: pars})
      .pipe(map(res => {
        return res as Post[];
      }));
  }

  getCount(groupId: string | null, keyword: string | null) {
    let pars: any = {};

    if(groupId != null)
      pars.groupId = groupId;
    if(keyword != null)
      pars.keyword = keyword;

    return this.httpClient.get<number>(this.url + '/get-count',
    {params: pars})
      .pipe(map(res => {
        return res;
      }));
  }

  get(postId: number): Observable<Post> {
    return this.httpClient.get<Post>(this.url + '/get',
    {params: {"postId": postId}})
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
    return this.httpClient.post(this.url + '/add',
      {'title': title, 'description': description, 'groupId': groupId})
      .pipe(take(1));
  }

  edit(postId: number, title: string, description: string) {
    return this.httpClient.post(this.url + '/edit',
      {'postId': postId, 'title': title, 'description': description})
      .pipe(take(1));
  }

  delete(postId: number) {
    this.httpClient.post(this.url + '/delete',
      {'postId': postId})
      .pipe(take(1)).subscribe();
  }
}
