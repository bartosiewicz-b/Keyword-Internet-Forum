import { take } from 'rxjs/operators';
import { AppUser } from './../model/AppUser';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  url = 'http://localhost:8080/user';

  constructor(private httpClient: HttpClient) { }

  get(username: string) {
    return this.httpClient.get<AppUser>(this.url + '/get',
    {params: {'username': username}})
    .pipe(take(1));

  }
}
