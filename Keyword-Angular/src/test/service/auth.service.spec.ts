import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { AuthService } from '../../app/service/auth.service';

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule
      ],
      providers: [
        AuthService
      ]
    });
    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should login user', () => {
    const tokenResponse = {
      token: 'token',
      refreshToken: 'refreshToken',
      username: 'username',
      email: 'email'
    };

    service.login('login', 'password').subscribe(res => {
      expect(res).toEqual(tokenResponse);
    });

    const request = httpMock.expectOne(`${service.url}/login`);

    expect(request.request.method).toBe('POST');

    request.flush(tokenResponse);
  });

  it('should register user', () => {

    service.register('email', 'username', 'password');

    const request = httpMock.expectOne(`${service.url}/register`);

    expect(request.request.method).toBe('POST');
  });
});