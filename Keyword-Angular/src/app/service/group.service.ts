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
    {params: {"page": page, "name": keyword}})
      .pipe(map(res => {
        return res as Group[];
      }));
  }

  getSubscribed(): Observable<Group[]> {
    return this.httpClient.get<Group[]>(this.url + '/get-subscribed')
      .pipe(map(res => {
        return res as Group[];
      }));
  }

  get(groupId: string): Observable<Group> {
    return this.httpClient.get<Group>(this.url + '/get',
    {params: {"id": groupId}})
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
    this.httpClient.post(this.url + '/create',
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
}
