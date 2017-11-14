import { Component, OnInit } from '@angular/core';
import {Router, ActivatedRoute } from '@angular/router';
import { HttpClient , HttpHeaders } from '@angular/common/http';

import { AlertService, AuthenticationService } from '../_services/index';
import { appConfig } from '../app.config';
import { CompanionComponent } from './companion.component';
import { Companion } from '../_models/companion'

@Component({
  moduleId: module.id,
  templateUrl: 'companion.parent.component.html'
})

// Class that will determine the compatibiliy of the plants in a garden. It will
// keep track of the compatibiliy of the plants using a 2D array, where each
// element will use a model from "companion.ts" which contains the name of the
// plant, the color status, the companion of the plant, and the enemy of the plant.
export class CompanionParentComponent implements OnInit {
  private row : number = 2;
  private column : number = 4;
  private plants : Companion[][];

  plantList: string[];

  constructor(
  private route: ActivatedRoute,
  private router: Router,
  private authenticationService: AuthenticationService,
  private alertService: AlertService,
  private http: HttpClient){}

  ngOnInit(){
    this.initializeArray();

    // Get list of plant names from the database
    this.http.get(appConfig.apiGetPlantsName).subscribe(
      data => {
        let plantData = data['data'];

        this.plantList = new Array<string>();
        for (var i = 0; i < plantData.length; i++)
          this.plantList.push(plantData[i]._id);
      },
      error => {
        console.log(error);
      }
    );
  }

  // Initializes the 2D array that contains plant information for the garden grid.
  initializeArray() {

    // Initialize the rows of the garden grid.
    this.plants = new Array(this.row);
    for (var i = 0; i < this.row; i++) {
      this.plants[i] = new Array<Companion>(this.column);
    }

    // Initialize the columns of the garden grid.
    for (var i = 0; i < this.row; i++) {
      for (var j = 0; j < this.column; j++) {
        this.plants[i][j] = new Companion();
      }
    }
  }

  // Callback function that will retrieve plant data from "companion.component.ts",
  // when a change is detected. The information will be stored at the row and column
  // assigned to the garden, into the 2D array representing the garden grid.
  getData(data:any) {
    // Parse the data retrieved from plant
    let name = data[0];
    let row = data[1];
    let col = data[2];

    // Assign the name of the plant
    this.plants[row][col].name = name;

    // Assign the color status of the plant.
    // If the name is blank...
    if (name = '') {
      //... assign the color lightgray, which means that it is empty
      this.plants[row][col].color = 'lightgray';
    } else {
      //... otherwise assign white, meaning a plant has been assigned.
      this.plants[row][col].color = 'white';
    }

    // Get the companions and enemies of the plant from the database
    let body = {plant: this.plants[row][col].name};

    // Get the companions of the plant from the database
    this.http.post(appConfig.apiGetCompanion, body).subscribe(
      data => {
        this.plants[row][col].companion = data['data'];
      },
      error => {
        this.alertService.error(error);
      }
    );

    // Get enemies of the plant from the database
    this.http.post(appConfig.apiGetEnemy, body).subscribe(
      data => {
        this.plants[row][col].enemy = data['data'];
      },
      error => {
        this.alertService.error(error);
      }
    );
  }

  // Checks the compatibility of the plants in the garden grid. This will go through
  // all the plants in the garden and check the neighboring plants to determine
  // compatibility.
  checkCompanion() {
    for (var i = 0; i < this.row; i++) {
      for (var j = 0; j < this.column; j++) {

        this.plants[i][j].color = 'white';

        let checkPosRow = i - 1;
        let checkPosCol = j - 1;

        // Check upper left neighboring plant, if the row and column index is
        // not out of bounds.
        if ((checkPosRow >= 0) && (checkPosCol >= 0))
          this.assignColor(checkPosRow, checkPosCol, i, j);

        // Check left neighboring plant, if the row and column index is not out
        // of bounds.
        checkPosRow = i;
        checkPosCol = j - 1;

        if ((checkPosRow >= 0) && (checkPosCol >= 0))
          this.assignColor(checkPosRow, checkPosCol, i, j);

        // Check lower left neighboring plant, if the row and column index is not
        // out of bounds.
        checkPosRow = i + 1;
        checkPosCol = j - 1;

        if ((checkPosRow < this.row) && (checkPosCol >= 0))
          this.assignColor(checkPosRow, checkPosCol, i, j);

        // Check top neighboring plant, if the row and column index is not out
        // of bounds.
        checkPosRow = i - 1;
        checkPosCol = j;

        if ((checkPosRow >= 0) && (checkPosCol >= 0))
          this.assignColor(checkPosRow, checkPosCol, i, j);

        // Check bottom neighboring plant, if the row and column index is not out
        // of bounds.
        checkPosRow = i + 1;
        checkPosCol = j;

        if ((checkPosRow < this.row) && (checkPosCol >= 0))
          this.assignColor(checkPosRow, checkPosCol, i, j);

        // Check upper right neighboring plant, if the row and column index is not
        // out of bounds.
        checkPosRow = i - 1;
        checkPosCol = j + 1;

        if ((checkPosRow >= 0) && (checkPosCol < this.column))
          this.assignColor(checkPosRow, checkPosCol, i, j);

        // Check right neighboring plant, if the row and column index is not
        // out of bounds.
        checkPosRow = i;
        checkPosCol = j + 1;

        if ((checkPosRow >= 0) && (checkPosCol < this.column))
          this.assignColor(checkPosRow, checkPosCol, i, j);

        // Check lower right neighboring plant, if the row and column index is
        // not out of bounds.
        checkPosRow = i + 1;
        checkPosCol = j + 1;

        if ((checkPosRow < this.row) && (checkPosCol < this.column))
          this.assignColor(checkPosRow, checkPosCol, i, j);
      }
    }
  }

  // Helper function that will assign the color status to a plant. If there is an
  // enemy plant nearby, red is assigned. If there is a companion nearby with no
  // enemies, green is assigned. If there is no enemies or companions nearby, yellow
  // is assigned.
  assignColor(checkRow: number, checkCol: number, row: number, col: number) {
    if (this.plants[row][col].color === 'red')
      return;

    // Assign references to plant, plant's enemies, and plant's companion
    let plantName = this.plants[row][col].name;
    let plantCheckEnemy = this.plants[checkRow][checkCol].enemy;
    let plantCheckCompanion = this.plants[checkRow][checkCol].companion;

    // Determine plant compatibility

    // If the target plant is an enemy of the current plant...
    if (this.plants[row][col].enemy.includes(this.plants[checkRow][checkCol].name)) {
      //... assign the color status of red.
      this.plants[row][col].color='red';

      // If the target plant is a companion of the current plant...
    } else if (this.plants[row][col].companion.includes(this.plants[checkRow][checkCol].name)) {
      // If the current color status is yellow...
      if (this.plants[row][col].color === 'yellow') {
        //... return, since the current plant can only be green of it is surrounded
        // by companions
        return;
      }

      //... otherwise, assign the color status green
      this.plants[row][col].color='green';
    } else {
      //... otherwise, assign the color status of yellow
      this.plants[row][col].color='yellow';
    }
  }
}
