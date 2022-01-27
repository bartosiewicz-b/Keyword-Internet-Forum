/*import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { CommentService } from './comment.service';
import { Comment } from '../model/comment';

describe('CommentService', () => {
  let service: CommentService;
  let httpMock: HttpTestingController;
  let comments: Comment[];

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule
      ],
      providers: [
        CommentService
      ]
    });
    service = TestBed.inject(CommentService);
    httpMock = TestBed.inject(HttpTestingController);

    comments = [new Comment(), new Comment()];
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should retrieve all comments', () => {
    service.getAll(0).subscribe(res => {
      expect(res.length).toBe(2);
      expect(res).toEqual(comments);
    });

    //const request = httpMock.expectOne(`${service.url}/get?postId=0`);

    //expect(request.request.method).toBe('GET');

    //request.flush(comments);
  });
});*/
