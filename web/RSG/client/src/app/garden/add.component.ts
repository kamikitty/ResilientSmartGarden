import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { AlertService, AuthenticationService } from '../_services/index';
import { appConfig } from '../app.config';
import { Garden } from '../_models/garden';

@Component({
  moduleId: module.id,
  templateUrl: 'garden.component.html'
})

export class GardenComponent {
  currentUser: string;
  gardens: Garden[];
  model: any = {};
  loading = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authenticationService: AuthenticationService,
    private alertService: AlertService,
    private http: HttpClient) {
      this.currentUser = localStorage.getItem('currentUser');
    }

  private addGarden() {
    this.http.post(appConfig.apiAddGarden, {mac: this.gardens[i].mac}).subscribe(
      data => {
        this.gardens[i].temperature = data['temperature'];
        this.gardens[i].humidity = data['humidity'];
        this.gardens[i].moisture = data['moisture'];
      },
      error => {
        console.log(error);
      }
    );
  }
}
