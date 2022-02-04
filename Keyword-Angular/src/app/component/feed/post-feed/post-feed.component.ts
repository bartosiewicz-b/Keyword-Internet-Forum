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

  searchPhrase: string = '';

  isError: boolean = false;
  isLoading: boolean = true;

  constructor(private postService: PostService,
    private router: Router,
    private route: ActivatedRoute) {
      this.updateSearchPhrase()
  }

  ngOnInit(): void {
    this.updateDisplay();
  }

  search() {
    if(this.searchPhrase != this.prevValue){
      this.updateDisplay();  

      this.prevValue = this.searchPhrase;
    }
    
  }

  updateDisplay(){
    this.route.queryParams.subscribe(res => {
      this.currentPage = res['page']!=null ? res['page'] : 1;
      

      this.postService.getAll(this.currentPage - 1, this.groupId, this.searchPhrase).pipe(take(1),)
      .subscribe(res => {
        this.posts = res
        this.isLoading = false;
      },
        err => {
          this.isError = true;
          this.isLoading = false;
        });
    })

    this.postService.getCount(this.groupId, this.searchPhrase).pipe(take(1))
    .subscribe(res => {
      this.pages =  Array(Math.floor(res/10 + 1));

      for(let i = 0; i < this.pages.length; i++)
        this.pages[i] = i + 1;
        
    }, err => {
      this.isError = true;
      this.isLoading = false;
    });
  }

  updateSearchPhrase() {
    this.route.queryParams.subscribe(res => {
      this.searchPhrase = res['keyword']!=null ? res['keyword'] : '';
    });
  }

  goToPage(i: number) {
    console.log(this.searchPhrase)
    let destination: string = '/';
    if(this.groupId!=null)
      destination = '/'  + this.groupId;

    this.router.navigate([destination], {queryParams: {page: i, keyword: this.searchPhrase}});
  }
}
