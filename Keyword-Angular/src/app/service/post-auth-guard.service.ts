import { PostService } from './post.service';
import { ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot } from '@angular/router';
import { Injectable } from '@angular/core';
import { AuthService } from './auth.service';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class PostAuthGuardService implements CanActivate{

  constructor(private authService: AuthService,
    private postService: PostService) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean>{

  return this.postService.get(route.params.postId)
    .pipe(map((res: any) => {
      return this.authService.getUsername() == res.username;
    }));
  }
}
