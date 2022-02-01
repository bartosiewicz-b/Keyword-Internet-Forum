import { AppUser } from './../../app/model/app-user';
import { Group } from './../../app/model/group';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { GroupService } from '../../app/service/group.service';

describe('GroupService', () => {
  let service: GroupService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule
      ],
      providers: [
        GroupService
      ]
    });
    service = TestBed.inject(GroupService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should add a group', () => {
    const group: Group = new Group;

    service.add(group.groupName, group.description).subscribe(res => {
      expect(res).toEqual(group);
    });

    const request = httpMock.expectOne(`${service.url}/add`);

    expect(request.request.method).toBe('POST');

    request.flush(group);
  });

  it('should retrieve all groups', () => {
    const groups: Group[] = [new Group];

    service.getAll(0, '').subscribe(res => {
      expect(res).toEqual(groups);
    });

    const request = httpMock.expectOne(`${service.url}/get-all?page=0&keyword=`);

    expect(request.request.method).toBe('GET');

    request.flush(groups);
  });

  it('should retrieve number of groups', () => {
    const count: number = 3;

    service.getCount('').subscribe(res => {
      expect(res).toEqual(count);
    });

    const request = httpMock.expectOne(`${service.url}/get-count?keyword=`);

    expect(request.request.method).toBe('GET');

    request.flush(count);
  });

  it('should retrieve a group', () => {
    const group: Group = new Group;

    service.get(group.id).subscribe(res => {
      expect(res).toEqual(group);
    });

    const request = httpMock.expectOne(`${service.url}/get?groupId=${group.id}`);

    expect(request.request.method).toBe('GET');

    request.flush(group);
  });

  it('should edit a group', () => {
    const group: Group = new Group;

    service.edit(group.id, group.groupName, group.description, group.avatarUrl).subscribe(res => {
      expect(res).toEqual(group);
    });

    const request = httpMock.expectOne(`${service.url}/edit`);

    expect(request.request.method).toBe('POST');

    request.flush(group);
  });

  it('should delete a group', () => {

    service.delete('groupId');

    const request = httpMock.expectOne(`${service.url}/delete`);

    expect(request.request.method).toBe('POST');
  });

  it('should transfer a group ownership', () => {

    service.transferOwnership('groupId', 'username');

    const request = httpMock.expectOne(`${service.url}/transfer-ownership`);

    expect(request.request.method).toBe('POST');
  });

  it('should retrieve all group subscribers', () => {
    const subscribers: AppUser[] = [new AppUser];

    service.getSubscribers('groupId', '').subscribe(res => {
      expect(res).toEqual(subscribers);
    });

    const request = httpMock.expectOne(`${service.url}/get-subscribers?groupId=groupId&keyword=`);

    expect(request.request.method).toBe('GET');

    request.flush(subscribers);
  });

  it('should subscribe a group', () => {

    service.subscribe('groupId',);

    const request = httpMock.expectOne(`${service.url}/subscribe`);

    expect(request.request.method).toBe('POST');
  });

  it('should retrieve all group moderators', () => {
    const moderators: AppUser[] = [new AppUser];

    service.getModerators('groupId').subscribe(res => {
      expect(res).toEqual(moderators);
    });

    const request = httpMock.expectOne(`${service.url}/get-moderators?groupId=groupId`);

    expect(request.request.method).toBe('GET');

    request.flush(moderators);
  });

  it('should add moderator to group', () => {

    service.addModerator('groupId', 'username');

    const request = httpMock.expectOne(`${service.url}/add-moderator`);

    expect(request.request.method).toBe('POST');
  });

  it('should remove moderator from group', () => {

    service.deleteModerator('groupId', 'username');

    const request = httpMock.expectOne(`${service.url}/delete-moderator`);

    expect(request.request.method).toBe('POST');
  });
});