import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { AlertService, AuthenticationService } from '../_services/index';
import { appConfig } from '../app.config';
import { Garden } from '../_models/garden';

// nodejs packages for IP addresses
import * as isReachable from 'is-reachable';
import * as address4 from 'ip-address';
import * as ip from 'address';

// const isReachable = require('is-reachable');
// const address4 = require('up-address').Address4;
// const internalIp = require('internal-ip');

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


  ngOnInit() {
    this.gardens = new Array<Garden>();
    this.http.post(appConfig.apiGetGardens, {username: this.currentUser}).subscribe(
      data => {
        for (var i = 0; i < data['gardens'].length; i++) {
          this.gardens.push(new Garden());
          this.gardens[i].name = data['gardens'][i]['name'];
          this.gardens[i].mac = data['gardens'][i]['mac'];

          this.updateSensors(i);
        }
      },
      error => {
        console.log(error);
      }
    );

    console.log(ip.ip());
  }

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

  private updateAllSensors() {
    for (var i = 0; i < this.gardens.length; i++)
      this.updateSensors(i);
  }
}
