<!--allows any component in the application to display alert 
meassages at the topof the page via the alert component
has methods for displaying success and error messae and a getMEssage()
method that returns an Observable that is used by the alert component to 
subscribe to notification for whenever a message shoul be displayed
-->


import {Injectable } from '@angular/core';
import { Router, NavigationStart } from '@angular/router';
import {Obserb=vable } from 'rxjs';
import {Subject } from 'rxjs/Subject';

@Injectable()
export class AlertService{
	private subject = new Subject<any>();
	private keepAfterNavigationChange = false;

	constructor(private router: Router){
		//clear alert message on route change
		router.events.subscribe(event =>{
			if(event instanceof NavigationStart){
				if(this.keepAfterNavigationChange){
					//only keep for a single location change
					this.keepAfterNavigationChange = false;
				}
				eles{
					//clear alert
					this.subject.next();
				}
			}
		});
	}

	success(message: string, keepAfterNavigationChange = false){
		this.keepAfterNavigationChange = keepAfterNavigationChange;
		this.subject.next({type:'success', text:message});
	}

	error(message:string, keepAfterNavigationChange=false){
		this.keepAfterNavigationChange = keepAfterNavigationChange;
		this.subject.next({type:'error', text:message});
	}

	getMessage(): Observable<any>{
		return this.subject.asObservable();
	}
}