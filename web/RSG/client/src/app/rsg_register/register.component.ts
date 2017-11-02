import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

import { AlertService, UserService } from '../_services/index';
import { appConfig } from '../app.config';

@Component({
	moduleId: module.id,
	templateUrl: 'register.component.html'
})

export class RegisterComponent {
	model: any = {};
	loading = false;

	constructor(
		private router: Router,
		private userService: UserService,
		private alertService: AlertService,
		private http: HttpClient) {}

	// Function that registers a user onto the database. This is called in
	// "register.component.html"
	register() {
		this.loading = true;

		// Check to see if password and confirm password is the same...
		if (this.model.password != this.model.confirmPassword) {
			//..if it's not the same, notify the user and return.
			this.alertService.error('Password does not match');
			this.loading = false;
			return;
		}

		let body = {username: this.model.username, password: this.model.password};

		// Attempt an HTTP POST request to register the user into the database
		this.http.post(appConfig.apiRegister, body).subscribe(
			data => {
				// If registration was successful...
				if (data['success']) {
					//... notify the user registration was successful and redirect to the login page
					this.alertService.success('Registration Successful!', true);
					this.router.navigate(['/login']);
				} else {
					//... otherwise the registration was unsuccessful, notify the user.
					this.alertService.error('Registration Failed!');
					this.loading = false;
				}
			},
			error => {
				// An error has occurred when making the HTTP Request
				this.alertService.error(error);
				this.loading = false;
			});
		}
}
