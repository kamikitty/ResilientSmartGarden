import { Component } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';

import { AlertService } from '../_services/index';
import { appConfig } from '../app.config';

@Component({
  moduleId: module.id,
  templateUrl: 'login.component.html'
})

export class LoginComponent {
  model: any ={};
  loading = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private alertService: AlertService,
    private http: HttpClient) {}

  // Function that logs the user into their account. This is called in "login.component.html".
  login(){
    this.loading = true;

    const body = {username: this.model.username, password: this.model.password};

    // Attempt an HTTP POST request to validate the user
    this.http.post(appConfig.apiLogin, body).subscribe(
      data => {
        // If the user credentials are valid...
        if (data['success']) {
          //... notify the user the login is successful and navigate to their gardens
          this.alertService.success('Login Sucessful!');
          this.loading = false;

          // Store the username locally to reference user's gardens
          localStorage.setItem('currentUser', this.model.username);

          // Navigates to the user's gardens
          this.router.navigate(['/garden']);
        } else {
          //... otherwise the user's credentials are invalid. Notify user.
          this.alertService.error('Login Failed!');
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
