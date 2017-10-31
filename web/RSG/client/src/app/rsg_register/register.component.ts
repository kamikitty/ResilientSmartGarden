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

	register() {
		this.loading = true;

		// Check to see if password and confirm password is the same
		if (this.model.password != this.model.confirmPassword) {
			this.alertService.error('Password does not match');
			this.loading = false;
			return;
		}

		let body = {username: this.model.username, password: this.model.password};

		this.http.post(appConfig.apiRegister, body).subscribe(
			data => {
				if (data['success']) {
					this.alertService.success('Registration Successful!', true);
					this.router.navigate(['/login']);
				} else {
					this.alertService.error('Registration Failed!');
					this.loading = false;
				}
			},
			error => {
				this.alertService.error(error);
				this.loading = false;
			});
		}
}
