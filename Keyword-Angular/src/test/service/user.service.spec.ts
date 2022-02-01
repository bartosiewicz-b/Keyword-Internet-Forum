import { Group } from './../../app/model/group';
import { AppUser } from '../../app/model/app-user';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { UserService } from 'src/app/service/user.service';


describe('UserService', () => {
  let service: UserService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule
      ],
      providers: [
        UserService
      ]
    });
    service = TestBed.inject(UserService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get user from username', () => {
    const user: AppUser = new AppUser;

    service.get(user.username).subscribe(res => {
      expect(res).toEqual(user);
    });

    const request = httpMock.expectOne(`${service.url}/get?username=${user.username}`);

    expect(request.request.method).toBe('GET');

    request.flush(user);
  });

  it('should get user subscribed groups from token', () => {
    const groups: Group[] = [new Group];

    service.getSubscribed().subscribe(res => {
      expect(res).toEqual(groups);
    });

    const request = httpMock.expectOne(`${service.url}/get-subscribed`);

    expect(request.request.method).toBe('GET');

    request.flush(groups);
  });
});