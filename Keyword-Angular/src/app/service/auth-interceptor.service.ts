import { HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthInterceptorService implements HttpInterceptor {

  constructor() { }

  intercept(req: HttpRequest<any>, next: HttpHandler) {

    if(sessionStorage.getItem('token')) {
      
      req = req.clone({
        headers: req.headers.set('Authorization', 'Bearer ' + sessionStorage.getItem('token'))
      });
    }

    return next.handle(req);
  }
}
