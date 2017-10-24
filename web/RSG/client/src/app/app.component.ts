
/*app component is the root component of the application, defines the root tag of the app as <app></app> with the selector property

The moduled property is set to allow a relative path to be use for the template Url*/


import { Component } from '@angular/core';

@Component({
	moduleId: module.id,
	selector: 'app',
	templateUrl 'app.component.html'
})

export class AppComponent{}

/*@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'SLOWLY GETTING THERE!!!!!!';
}
*/

