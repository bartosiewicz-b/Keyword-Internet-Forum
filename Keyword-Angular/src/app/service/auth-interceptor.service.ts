import { MemoryService } from './memory.service';
import { HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthInterceptorService implements HttpInterceptor {

  constructor(private memoryService: MemoryService) { }

  intercept(req: HttpRequest<any>, next: HttpHandler) {

    if(this.memoryService.getToken() != null) {

      req = req.clone({
        headers: req.headers.set('Authorization', 'Bearer ' + this.memoryService.getToken())
      });
    }

    return next.handle(req);
  }
}
