import { Post } from '../../app/model/post';
import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing'
import { PostService } from '../../app/service/post.service';

describe('PostService', () => {
  let service: PostService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule
      ],
      providers: [
        PostService 
      ]
    });
    service = TestBed.inject(PostService);
    httpMock = TestBed.inject(HttpTestingController);
    });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should add a post', () => {
    const post: Post = new Post;

    service.add(post.title, post.description, post.groupId).subscribe(res => {
      expect(res).toEqual(post);
    });

    const request = httpMock.expectOne(`${service.url}/add`);

    expect(request.request.method).toBe('POST');

    request.flush(post);
  });

  it('should retrieve all posts', () => {
    const posts: Post[] = [new Post];

    service.getAll(0, posts[0].groupId, '').subscribe(res => {
      expect(res).toEqual(posts);
    });

    const request = httpMock.expectOne(`${service.url}/get-all?page=0&groupId=${posts[0].groupId}&keyword=`);

    expect(request.request.method).toBe('GET');

    request.flush(posts);
  });

  it('should retrieve number of posts', () => {
    const count: number = 3;

    service.getCount('groupId', '').subscribe(res => {
      expect(res).toEqual(count);
    });

    const request = httpMock.expectOne(`${service.url}/get-count?groupId=groupId&keyword=`);

    expect(request.request.method).toBe('GET');

    request.flush(count);
  });

  it('should retrieve a post', () => {
    const post: Post = new Post;

    service.get(post.id).subscribe(res => {
      expect(res).toEqual(post);
    });

    const request = httpMock.expectOne(`${service.url}/get?postId=${post.id}`);

    expect(request.request.method).toBe('GET');

    request.flush(post);
  });

  it('should edit a post', () => {
    const post: Post = new Post;

    service.edit(post.id, post.title, post.description).subscribe(res => {
      expect(res).toEqual(post);
    });

    const request = httpMock.expectOne(`${service.url}/edit`);

    expect(request.request.method).toBe('POST');

    request.flush(post);
  });

  it('should delete a post', () => {

    service.delete(1);

    const request = httpMock.expectOne(`${service.url}/delete`);

    expect(request.request.method).toBe('POST');
  });

  it('should upvote a post', () => {
    const votes: number = 10;

    service.upvote(1).subscribe(res => {
      expect(res).toEqual(votes);
    });

    const request = httpMock.expectOne(`${service.url}/upvote`);

    expect(request.request.method).toBe('POST');

    request.flush(votes);
  });

  it('should downvote a post', () => {
    const votes: number = 10;

    service.downvote(1).subscribe(res => {
      expect(res).toEqual(votes);
    });

    const request = httpMock.expectOne(`${service.url}/downvote`);

    expect(request.request.method).toBe('POST');

    request.flush(votes);
  });
});