import { ActivatedRoute, Router } from '@angular/router';
import { GroupService } from './../../service/group.service';
import { Group } from './../../model/group';
import { AppUser } from '../../model/app-user';
import { Component, OnInit } from '@angular/core';
import { take } from 'rxjs/operators';

@Component({
  selector: 'app-manage-group',
  templateUrl: './manage-group.component.html',
  styleUrls: ['./manage-group.component.css']
})
export class ManageGroupComponent implements OnInit {

  group: Group = new Group();
  moderators: AppUser[] = [];

  findUsers: AppUser[] = [];

  addingModerator: boolean = false;
  selectId: number | null = null;
  searchPhrase: string = '';

  transferingOwnership: boolean = false;

  constructor(private groupService: GroupService,
    private route: ActivatedRoute,
    private router: Router) { 
      let groupId = this.route.snapshot.paramMap.get('groupId');

      groupService.get(groupId!=null ? groupId : "").pipe(take(1)).subscribe(res => {
        this.group = res;

        groupService.getModerators(groupId!=null ? groupId : "").pipe(take(1)).subscribe(mods => {
          this.moderators = mods;
        });

        groupService.getSubscribers(groupId!=null ? groupId : "", this.searchPhrase).pipe(take(1)).subscribe(users => {
          this.findUsers = users;
        });
      });
    }

  ngOnInit(): void {
  }

  initAddModeratorSelection() {
    this.addingModerator = true;
    this.transferingOwnership = false;

    this.selectId = null;
    this.searchPhrase = '';
  }

  initTransferOwnershipSelection() {
    this.transferingOwnership = true;
    this.addingModerator = false;

    this.selectId = null;
    this.searchPhrase = '';
  }

  addModerator() {
    if(this.selectId!=null) {
      this.groupService.addModerator(this.group.id, this.findUsers[this.selectId].username);
      this.moderators.push(this.findUsers[this.selectId]);
    }
      

    this.addingModerator = false;
    this.selectId = null;
  }

  removeModerator(user: AppUser) {
    this.groupService.deleteModerator(this.group.id, user.username);

    this.moderators.splice(this.moderators.indexOf(user), 1);
  }

  transferOwnership() {
    if(this.selectId != null) {
      this.groupService.transferOwnership(this.group.id, this.moderators[this.selectId].username);
      this.router.navigateByUrl('/' + this.group.id);
    }
    
    this.transferingOwnership = false;
    this.selectId = null;
  }

  searchUser() {
    this.groupService.getSubscribers(this.group.id, this.searchPhrase).pipe(take(1)).subscribe(users => {
      this.findUsers = users;
      this.selectId = null;
    });
  }
}
