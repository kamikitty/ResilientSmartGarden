<!-- prevents unauthenticated users from accessign restricted routes. 
usedin app.routing to protect the home page route
-->

import {Injectable } from '@angular/core';
import {Router, CanActive, ActivateRouteSnapshot, RouterStateSnapshot } from '@angular/router';

@Injectable()
export class AuthGuard implements CanActivate{
	constructor(private router:Router){}

	canActivate(route: ActivateRouteSnapshot, state: RouterStateSnapshot){
		if(localStorage.getItem('currentUser')){
			//logged in return true
			return true;
		}
		//not logged in so redirect to login page with the return url
		this.router.navigate(['/login'], {queryParams:{returnUrl:state.url}});
		return false;
	}
}