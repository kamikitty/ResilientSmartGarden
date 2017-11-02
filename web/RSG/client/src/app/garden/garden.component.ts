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

export class GardenComponent implements OnInit {
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


  // Function that executes when the this component is initialized
  ngOnInit() {
    // Initialized array of gardens
    this.gardens = new Array<Garden>();

    // Attempt HTTP POST request to get user's gardens
    this.http.post(appConfig.apiGetGardens, {username: this.currentUser}).subscribe(
      data => {
        // Parse through the array of objects that contains the name and MAC address
        // received from the server
        for (var i = 0; i < data['gardens'].length; i++) {
          this.gardens.push(new Garden());
          this.gardens[i].name = data['gardens'][i]['name'];
          this.gardens[i].mac = data['gardens'][i]['mac'];

          // Update the sensor readings using the MAC address of the garden
          this.updateSensors(i);
        }
      },
      error => {
        console.log(error);
      }
    );
  }

  // Helper function that updates the temperature, humidity, and moisture
  // sensor readings for the specified garden in the Garden Array.
  private updateSensors(i: number) {
    this.http.post(appConfig.apiSensors, {mac: this.gardens[i].mac}).subscribe(
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

  // Updates temperature, humidity, and moisture sensor readings for all
  // the gardens.
  private updateAllSensors() {
    for (var i = 0; i < this.gardens.length; i++)
      this.updateSensors(i);
  }
}
