import { Group } from './../../../model/group';
import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'group-card',
  template: `<div class="card">
              <div class="card-body">
                <div>
                  <small class="text-muted">Subscribers: {{group.subscriptions}}</small>
                  <h5 class="card-title">{{group.groupName}}</h5>
                </div>
                <p class="card-text">{{group.description|excerpt:1150}}</p>
              </div>
            </div>`,
  styles: [`.card{border: 1px solid black;}`]
})
export class GroupCardComponent {
  @Input('group') group: Group = new Group;
}
