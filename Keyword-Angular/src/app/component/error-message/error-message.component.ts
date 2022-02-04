import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'error-message',
  template: `<div class="content">
              <h1>Oops... Something went wrong :(</h1>
              <p>Please, refresh the page.</p>
            </div>`,
  styles: [`.content{margin-top: 100px;text-align: center;}`]
})
export class ErrorMessageComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }

}
