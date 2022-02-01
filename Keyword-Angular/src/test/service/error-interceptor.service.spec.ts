import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { AuthService } from '../../app/service/auth.service';
import { TestBed } from '@angular/core/testing';

import { ErrorInterceptorService } from '../../app/service/error-interceptor.service';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';

describe('ErrorInterceptorService', () => {
  let service: ErrorInterceptorService;
  let httpMock: HttpTestingController;
  let httpClient: HttpClient;
  const authService = jasmine.createSpyObj('AuthService', ['getToken']);


  beforeEach(() => {
    authService.getToken.and.returnValue('token');

    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule
      ],
      providers: [
        ErrorInterceptorService,
        { provide: AuthService, useValue: authService }
      ]
    });
    service = TestBed.inject(ErrorInterceptorService);
    httpMock = TestBed.inject(HttpTestingController);
    httpClient = TestBed.inject(HttpClient);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('When 401, intercept', () => {

    httpClient.get('/url').subscribe(
      res => fail('401 error'),
      (error: HttpErrorResponse) => {
        expect(error.status).toBe(401);
      })

    const req = httpMock.expectOne('/url');

    req.flush('401 error', {status: 401, statusText: 'Unauthorized'});
  });
  
});
