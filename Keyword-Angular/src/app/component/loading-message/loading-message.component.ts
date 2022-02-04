import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'loading-message',
  template: `<div class="content">
              <div class="spinner-border" role="status">
                <span class="sr-only"></span>
              </div>
              <h1>Cheap servers are sleepy :)</h1>
              <p>Give them a couple of seconds to wake up</p>
            </div>`,
  styles: [`.content{margin-top: 100px;text-align: center;}`]
})
export class LoadingMessageComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }

}
