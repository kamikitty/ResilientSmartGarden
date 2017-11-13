import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { AlertService, AuthenticationService } from '../_services/index';
import { appConfig } from '../app.config';

@Component({
  moduleId: module.id,
  selector: 'companion-plant',
  templateUrl: 'companion.component.html'
})

// Class that will maintain plant information on the companion planting layout.
// There is 3 inputs: plantList to get the list of plant names, posRow to get the
// row location of the plant, and posCol to get the column location of the plant.
// The list of plants names is retrieved from "companion.parent.component.ts".
// The row position and column position is retrieved from "companion.parent.component.html".
// The outout will send the plant name, row position, and column position to
// "companion.parent.component.ts" when a change is detected.
export class CompanionComponent {
  selectedPlant: string = '';
  @Input() plantList: string[];
  @Input() posRow: number;
  @Input() posCol: number;
  @Output() sendData: EventEmitter <any[]>= new EventEmitter<any[]>();

  // Callback function that will send the plant's position in the garden as row
  // and column, and the plant name.
  onChange() {
    let params = new Array();

    // Prepare data to send to parent component
    let plant = this.selectedPlant;
    let row = this.posRow;
    let col = this.posCol;

    // Store the data into an array to send to parent component
    params.push(plant);
    params.push(row);
    params.push(col);

    // Send the data to parent component
    this.sendData.emit(params);
  }
}
