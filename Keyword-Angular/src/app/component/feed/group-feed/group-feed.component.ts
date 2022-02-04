import { ActivatedRoute, Router } from '@angular/router';
import { GroupService } from './../../../service/group.service';
import { Group } from './../../../model/group';
import { Component, OnInit } from '@angular/core';
import { take } from 'rxjs/operators';

@Component({
  selector: 'group-feed',
  templateUrl: './group-feed.component.html',
  styleUrls: ['./group-feed.component.css']
})
export class GroupFeedComponent implements OnInit {

  prevValue: string = '';
  groups: Group[] = [];

  pages: number[] = [];
  currentPage: number = 0;

  searchPhrase: string = '';

  isError: boolean = false;
  isLoading: boolean = true;

  constructor(private groupService: GroupService,
    private route: ActivatedRoute,
    private router: Router) {
      this.updateSearchPhrase();
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

  updateDisplay() {
    this.route.queryParams.subscribe(res => {
      this.currentPage = res['page']!=null ? res['page'] : 1;

      this.groupService.getAll(this.currentPage - 1, this.searchPhrase).pipe(take(1),)
      .subscribe(res => {
        this.groups = res;
        this.isLoading = false;
      },
        err => {
          this.isError = true;
          this.isLoading = false;
        });
    })

    this.groupService.getCount(this.prevValue).pipe(take(1))
    .subscribe(res => {
      this.pages =  Array(Math.floor(res/10 + 1));

      for(let i = 0; i < this.pages.length; i++)
        this.pages[i] = i + 1;

        this.isLoading = false;
    },err => {
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
    this.router.navigate(['/groups'], {queryParams: {page: i, keyword: this.searchPhrase}});
  }
}
