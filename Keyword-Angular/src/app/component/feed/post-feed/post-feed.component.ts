import { Router, ActivatedRoute } from '@angular/router';
import { take } from 'rxjs/operators';
import { Component, Input, OnInit } from '@angular/core';
import { Post } from '../../../model/post';
import { PostService } from '../../../service/post.service';

@Component({
  selector: 'post-feed',
  templateUrl: './post-feed.component.html',
  styleUrls: ['./post-feed.component.css']
})
export class PostFeedComponent implements OnInit {
  @Input('groupId') groupId: string | null = null;

  prevValue: string = '';
  posts: Post[] = [];

  pages: number[] = [];
  currentPage: number = 0;

  constructor(private postService: PostService,
    private router: Router,
    private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe(res => {
      this.currentPage = res['page']!=null ? res['page'] : 1;

      this.postService.getAll(this.currentPage - 1, this.groupId, '').pipe(take(1),)
      .subscribe(res => this.posts = res);

    })

    

    this.postService.getCount(null, null).pipe(take(1))
    .subscribe(res => {
      this.pages =  Array(Math.floor(res/10 + 1));

      for(let i = 0; i < this.pages.length; i++)
        this.pages[i] = i + 1;
    });
  }

  search(value: string) {
    if(value != this.prevValue){
      if(value.length > 3) 
        this.postService.getAll(0, this.groupId, value).pipe(take(1),).subscribe(res => {this.posts = res});
      else if(this.prevValue.length > 3)
        this.postService.getAll(0, this.groupId, '').pipe(take(1),).subscribe(res => {this.posts = res});

      this.prevValue = value;
    }
    
  }

  goToPage(i: number) {
    this.router.navigate(['/'], {queryParams: {page: i}});
  }
}
