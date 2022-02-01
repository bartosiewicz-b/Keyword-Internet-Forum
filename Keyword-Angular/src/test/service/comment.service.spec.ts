import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { CommentService } from 'src/app/service/comment.service';
import { Comment } from 'src/app/model/comment';

describe('CommentService', () => {
  let service: CommentService;
  let httpMock: HttpTestingController;

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
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should add a comment', () => {
    const comment: Comment = new Comment;

    service.add(comment.content, comment.postId, null).subscribe(res => {
      expect(res).toEqual(comment);
    });

    const request = httpMock.expectOne(`${service.url}/add`);

    expect(request.request.method).toBe('POST');

    request.flush(comment);
  });

  it('should retrieve all comments', () => {
    const comments: Comment[] = [new Comment];

    service.getAll(comments[0].postId).subscribe(res => {
      expect(res).toEqual(comments);
    });

    const request = httpMock.expectOne(`${service.url}/get-all?postId=${comments[0].postId}`);

    expect(request.request.method).toBe('GET');

    request.flush(comments);
  });

  it('should edit a comment', () => {

    service.edit(1, 'new content');

    const request = httpMock.expectOne(`${service.url}/edit`);

    expect(request.request.method).toBe('POST');
  });

  it('should delete a comment', () => {

    service.delete(1);

    const request = httpMock.expectOne(`${service.url}/delete`);

    expect(request.request.method).toBe('POST');
  });

  it('should upvote a comment', () => {
    const votes: number = 10;

    service.upvote(1).subscribe(res => {
      expect(res).toEqual(votes);
    });

    const request = httpMock.expectOne(`${service.url}/upvote`);

    expect(request.request.method).toBe('POST');

    request.flush(votes);
  });

  it('should downvote a comment', () => {
    const votes: number = 10;

    service.downvote(1).subscribe(res => {
      expect(res).toEqual(votes);
    });

    const request = httpMock.expectOne(`${service.url}/downvote`);

    expect(request.request.method).toBe('POST');

    request.flush(votes);
  });
});
