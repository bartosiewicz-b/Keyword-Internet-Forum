import { AppUser } from './../model/AppUser';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map, take } from 'rxjs/operators';
import { Group } from '../model/group';

@Injectable({
  providedIn: 'root'
})
export class GroupService {

  url = 'http://localhost:8080/group';

  constructor(private httpClient: HttpClient) { }

  getAll(page: number, keyword: string): Observable<Group[]> {
    return this.httpClient.get<Group[]>(this.url + '/get-all',
    {params: {"page": page, "keyword": keyword}})
      .pipe(map(res => {
        return res as Group[];
      }));
  }

  getCount(keyword: string | null) {
    let pars: any = {};

    if(keyword != null)
      pars.keyword = keyword;

    return this.httpClient.get<number>(this.url + '/get-count',
    {params: pars})
      .pipe(map(res => {
        return res;
      }));
  }

  getSubscribed(): Observable<Group[]> {
    return this.httpClient.get<Group[]>('http://localhost:8080/user/get-subscribed')
      .pipe(map(res => {
        return res as Group[];
      }));
  }

  get(groupId: string): Observable<Group> {
    return this.httpClient.get<Group>(this.url + '/get',
    {params: {"groupId": groupId}})
    .pipe(map(res => {
      return res as Group;
    }));
  }

  subscribe(groupId: string) {
    this.httpClient.post(this.url + '/subscribe', {'groupId': groupId})
      .pipe(take(1))
      .subscribe();
  }

  createGroup(groupName: string, description: string) {
    this.httpClient.post(this.url + '/add',
      {'groupName': groupName, 'description': description})
      .pipe(take(1))
      .subscribe();
  }

  editGroup(id: string, groupName: string, description: string) {
    this.httpClient.post(this.url + '/edit',
      {'id': id, 'groupName': groupName, 'description': description})
      .pipe(take(1))
      .subscribe();
  }

  deleteGroup(groupId: string) {
    this.httpClient.post(this.url + '/delete',
      {'groupId': groupId})
      .pipe(take(1))
      .subscribe();
  }

  getModerators(groupId: string): Observable<AppUser[]> {
    return this.httpClient.get<AppUser[]>(this.url + '/get-moderators',
    {params: {"groupId": groupId}})
    .pipe(map(res => {
      return res;
    }));
  }

  transferOwnership(groupId: string, username: string) {
    this.httpClient.post(this.url + '/transfer-ownership',
      {'groupId': groupId, 'username': username})
      .pipe(take(1))
      .subscribe();
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

  getSubscribers(groupId: string, username: string): Observable<AppUser[]> {
    return this.httpClient.get<AppUser[]>(this.url + '/get-subscribers',
    {params: {"groupId": groupId, "keyword": username}})
    .pipe(map(res => {
      return res;
    }));
  }
}
