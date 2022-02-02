import { environment } from './../../environments/environment';
import { map, take } from 'rxjs/operators';
import { AppUser } from '../model/app-user';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Group } from '../model/group';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  url = environment.baseUrl + '/user';

  constructor(private httpClient: HttpClient) { }

  get(username: string): Observable<AppUser> {
    return this.httpClient.get<AppUser>(this.url + '/get',
    {params: {'username': username}})
    .pipe(take(1));
  }

  getSubscribed(): Observable<Group[]> {
    return this.httpClient.get<Group[]>(this.url + '/get-subscribed')
      .pipe(take(1), map(res => {
        return res;
      }));
  }
}
