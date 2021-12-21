import { PostService } from './../../../service/post.service';
import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-create-post',
  templateUrl: './create-post.component.html',
  styleUrls: ['./create-post.component.css']
})
export class CreatePostComponent implements OnInit {

  constructor(private route: ActivatedRoute,
    private router: Router,
    private postService: PostService) { }

  ngOnInit(): void {
  }

  create(data: NgForm){

    let groupId = this.route.snapshot.paramMap.get('groupId');

    if(groupId != null) {
      this.postService.create(data.value.title, data.value.description, groupId);
      this.router.navigate(['/']);
    }
  }
}
