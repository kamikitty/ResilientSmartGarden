import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { AlertService, AuthenticationService } from '../_services/index';
import { appConfig } from '../app.config';
import { Garden } from '../_models/garden';

@Component({
  moduleId: module.id,
  templateUrl: 'add.garden.component.html'
})

export class AddGardenComponent {
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

  // Function that adds a garden to a user's account. This is called in
  // "add.component.html".
  addGarden() {
    const body = {username: this.currentUser,
      gardenname: this.model.gardenname,
      mac: this.model.mac};

    // Attempt an HTTP POST request to add a garden
    this.http.post(appConfig.apiAddGarden, body).subscribe(
      data => {
        // If adding a garden was successful...
        if (data['success']) {
          //... notify the user adding the garden was successful and navigate
          // back to the user's gardens
          this.alertService.success('Garden Added!');
          this.loading = false;
          this.router.navigate(['/garden']);
        } else {
          //... otherwise a duplicate was found in the database. Notify the user.
          this.alertService.error('Duplicate Garden Found!');
          this.loading = false;
        }
      },
      error => {
        console.log(error);
        this.loading = false;
      }
    );
  }
}
