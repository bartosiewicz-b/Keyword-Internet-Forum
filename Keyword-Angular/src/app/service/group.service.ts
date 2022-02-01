import { BACKEND_URL } from './../url';
import { AppUser } from '../model/app-user';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map, take } from 'rxjs/operators';
import { Group } from '../model/group';

@Injectable({
  providedIn: 'root'
})
export class GroupService {

  url = BACKEND_URL + '/group';

  constructor(private httpClient: HttpClient) { }

  add(groupName: string, description: string): Observable<Group> {
    return this.httpClient.post<Group>(this.url + '/add',
      {'groupName': groupName, 'description': description})
      .pipe(take(1));
  }

  getAll(page: number, keyword: string): Observable<Group[]> {
    return this.httpClient.get<Group[]>(this.url + '/get-all',
    {params: {"page": page, "keyword": keyword}})
      .pipe(take(1), map(res => {
        return res as Group[];
      }));
  }

  getCount(keyword: string | null): Observable<number> {
    let pars: any = {'keyword': keyword};

    return this.httpClient.get<number>(this.url + '/get-count',
    {params: pars})
      .pipe(take(1), map(res => {
        return res;
      }));
  }

  get(groupId: string): Observable<Group> {
    return this.httpClient.get<Group>(this.url + '/get',
    {params: {"groupId": groupId}})
    .pipe(take(1), map(res => {
      return res as Group;
    }));
  }

  edit(id: string, groupName: string, description: string, avatarUrl: string): Observable<Group> {
    return this.httpClient.post<Group>(this.url + '/edit',
      {'id': id, 'groupName': groupName, 'description': description, 'avatarUrl': avatarUrl})
      .pipe(take(1));
  }

  delete(groupId: string) {
    this.httpClient.post(this.url + '/delete', groupId)
      .pipe(take(1))
      .subscribe();
  }

  transferOwnership(groupId: string, username: string) {
    this.httpClient.post(this.url + '/transfer-ownership',
      {'groupId': groupId, 'username': username})
      .pipe(take(1))
      .subscribe();
  }

  getSubscribers(groupId: string, keyword: string): Observable<AppUser[]> {
    return this.httpClient.get<AppUser[]>(this.url + '/get-subscribers',
    {params: {"groupId": groupId, "keyword": keyword}})
    .pipe(take(1), map(res => {
      return res;
    }));
  }

  subscribe(groupId: string) {
    this.httpClient.post(this.url + '/subscribe', groupId)
      .pipe(take(1))
      .subscribe();
  }

  getModerators(groupId: string): Observable<AppUser[]> {
    return this.httpClient.get<AppUser[]>(this.url + '/get-moderators',
    {params: {"groupId": groupId}})
    .pipe(take(1), map(res => {
      return res;
    }));
  }

  addModerator(groupId: string, username: string) {
    this.httpClient.post(this.url + '/add-moderator',
      {'groupId': groupId, 'username': username})
      .pipe(take(1))
      .subscribe();
  }

  deleteModerator(groupId: string, username: string) {
    this.httpClient.post(this.url + '/delete-moderator',
      {'groupId': groupId, 'username': username})
      .pipe(take(1))
      .subscribe();
  }
}
