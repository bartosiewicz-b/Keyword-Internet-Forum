import { BACKEND_URL } from './../url';
import { map, take } from 'rxjs/operators';
import { AppUser } from './../model/AppUser';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Group } from '../model/group';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private url = BACKEND_URL + '/user';

  constructor(private httpClient: HttpClient) { }

  get(username: string): Observable<AppUser> {
    return this.httpClient.get<AppUser>(this.url + '/get',
    {params: {'username': username}})
    .pipe(take(1));
  }

  getSubscribed(): Observable<Group[]> {
    return this.httpClient.get<Group[]>(this.url + '/get-subscribed')
      .pipe(take(1), map(res => {
        return res as Group[];
      }));
  }
}
