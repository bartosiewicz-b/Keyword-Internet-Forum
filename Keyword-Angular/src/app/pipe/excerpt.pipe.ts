import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'excerpt'
})
export class ExcerptPipe implements PipeTransform {

  transform(text: string, length: any) {
    if(!text || !length)
      return text;

    if(text.length > length)
      return text.substr(0, length) + '...';

    return text;
  }

}
