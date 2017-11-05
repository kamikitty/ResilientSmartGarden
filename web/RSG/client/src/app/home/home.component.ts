/* home component gets the current user from local storage and all users from user the user service, and makes them available to the template */

import { Component } from '@angular/core';
import { User } from '../_models/index';
import { UserService } from '../_services/index';

@Component({
	moduleId: module.id,
	templateUrl: 'home.component.html'
})

export class HomeComponent {}
