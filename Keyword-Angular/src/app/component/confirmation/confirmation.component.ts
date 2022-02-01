import { Component, EventEmitter, HostBinding, Input, Output } from '@angular/core';

@Component({
  selector: 'confirmation',
  templateUrl: './confirmation.component.html',
  styleUrls: ['./confirmation.component.css']
})
export class ConfirmationComponent {
  @HostBinding('attr.class') cssClass = 'confirmation';
  @Input('message') message: string = '';
  @Output('confirm') confirm: EventEmitter<boolean> = new EventEmitter();
}
