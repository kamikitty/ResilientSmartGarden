/* defines the routes of the application, each route contains a path and associated component
home route is secured by passibng the AuthGuard to the canActivate property of the route */

import { Routes, RouterModule } from '@angular/router';

import { HomeComponent } from './home/index';
import { LoginComponent } from './rsg_login/index';
import { RegisterComponent } from './rsg_register/index';
import { GardenComponent, AddGardenComponent } from './garden/index';
import { CompanionComponent } from './companion/index';
import { AuthGuard } from './_guards/index';

const appRoutes: Routes = [
	{path: '', component: HomeComponent, canActivate: [AuthGuard]},
	{ path: 'login', component: LoginComponent },
	{ path: 'register', component: RegisterComponent },
	{ path: 'garden', component: GardenComponent },
	{ path: 'addGarden', component: AddGardenComponent },
	{ path: 'companion', component: CompanionComponent},

	//otherwise redirect to home
	{path: '**', redirectTo: ''}
];

export const routing = RouterModule.forRoot(appRoutes);
