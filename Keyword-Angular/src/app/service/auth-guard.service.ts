import { GroupService } from './group.service';
import { MemoryService } from './memory.service';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthGuardService implements CanActivate {

  constructor(private memoryService: MemoryService,
    private groupService: GroupService) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean>{

    return this.groupService.get(route.params.groupId)
    .pipe(map((res: any) => {
      return this.memoryService.getUsername() == res.owner;
    }));
  }
}
