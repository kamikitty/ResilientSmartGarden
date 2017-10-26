/* defines the routes of the application, each route contains a path and associated component
home route is secured by passibng the AuthGuard to the canActivate property of the route */

import { Routes, RouterModule } from '@angular/router';

import { HomeComponent } from './home/index';
import { LoginComponent } from './login/index';
import { RegisterComponent } from './register/index';
import {AuthGuard } from './_guards/index';

const appRoutes: Routes = [
	{path: '', component: HomeComponent, canActivate: [AuthGuard]},
	{ path: 'login', component: LoginComponent },
	{ path: 'register', component: RegisterComponent },

	//otherwise redirect to home
	{path: '**', redirectTo: ''}


];

export const routing = RouterModule.forRoot(appRoutes);
