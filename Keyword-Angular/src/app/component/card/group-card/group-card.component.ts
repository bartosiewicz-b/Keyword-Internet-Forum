import { Group } from './../../../model/group';
import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'group-card',
  templateUrl: './group-card.component.html',
  styleUrls: ['./group-card.component.css']
})
export class GroupCardComponent implements OnInit {
  @Input('group') group: Group = {} as Group;

  constructor() { }

  ngOnInit(): void {
  }

}
