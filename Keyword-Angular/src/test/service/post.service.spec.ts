import { Post } from '../../app/model/post';
import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing'
import { PostService } from '../../app/service/post.service';

describe('PostService', () => {
  //let service: PostService;
  //let httpMock: HttpTestingController;
  //let post: Post;

  beforeEach(() => {
    /*TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule
      ],
      providers: [
        PostService 
      ]
    });
    service = TestBed.inject(PostService);
    httpMock = TestBed.inject(HttpTestingController);

    post = new Post();*/
    TestBed.configureTestingModule({
      providers: [
      ]
    });

  });

  afterEach(() => {
    //httpMock.verify();
  });

  it('should be created', () => {
    //expect(service).toBeTruthy();
  });

  /*it('should retrieve all posts', () => {
    let posts = [post, post];

    service.getAll(0, '', '').subscribe(res => {
      expect(res.length).toBe(2);
      expect(res).toEqual(posts);
    });

    const request = httpMock.expectOne(`${service.url}/get-all?page=0&name=`);

    expect(request.request.method).toBe('GET');

    request.flush(posts);
  });

  it('should retrieve specific post', () => {

    service.get(0).subscribe(res => {
      expect(res).toEqual(post);
    });

    const request = httpMock.expectOne(`${service.url}/get?id=0`);

    expect(request.request.method).toBe('GET');

    request.flush(post);
  });*/
});