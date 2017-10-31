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

  login(){
    this.loading = true;

    const body = {username: this.model.username, password: this.model.password};

    this.http.post(appConfig.apiLogin, body).subscribe(
      data => {
        if (data['success']) {
          this.alertService.success('Login Sucessful!');
          this.loading = false;
          localStorage.setItem('currentUser', this.model.username);
          this.router.navigate(['/garden']);
        } else {
          this.alertService.error('Login Failed!');
          this.loading = false;
        }
      },
      error => {
        this.alertService.error(error);
        this.loading = false;
      });
  }
}
