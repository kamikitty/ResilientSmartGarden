
<!-- passes alert message to the template whenever a message is recieved from alert service-->
import { Component, OnInit} fron '@angular/core';
import {AlertService } from './_services/index';

@Component({
	moduleId: module.id,
	selector: 'alert',
	templateUrl:'alert.component.html'
})

export class AlertComponent{
	message:any;
	constructor(private alertService: AlertService){}

	ngOnInit(){
		this.alertService.getMessage().subscribe(message => {this.message=message;});
	}
}