import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { AlertService, AuthenticationService } from '../_services/index';
import { appConfig } from '../app.config';

@Component({
  moduleId: module.id,
  templateUrl: 'companion.component.html'
})

export class CompanionComponent implements OnInit {
  currentUser: string;
  loading = false;
  plants: any={};

  constructor(
  private route: ActivatedRoute,
  private router: Router,
  private authenticationService: AuthenticationService,
  private alertService: AlertService,
  private http: HttpClient){}

  ngOnInit() {
    this.http.get(appConfig.apiGetPlants).subscribe(
      data => {
        this.plants = data['plants'];

        console.log(this.plants.length);

        for (var i = 0; i < this.plants.length; i++)
          console.log(this.plants[i]._id);
      },
      error => {
        console.log(error);
      }
    );
  }
}
