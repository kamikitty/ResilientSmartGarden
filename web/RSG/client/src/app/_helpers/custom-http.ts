<!-- custom http service extends the default http service to add the following features:
1. automatically adds the JWT token (if looged in) to the http authorixzation header of all requests
2. prepends request urls with the api url from the appConfig file
3. Intercept 401 unauthorized responses from the api to automatically logout the user
-->

import { Injectable } from "@angular/core";
import { ConnectionBackend, XHRBackend, RequestOptions, Request, RequestOptionArgs, Response, Http, Headers } from "@angular/http";
import { appConfig } from '../app.config';

import { Observable } from "rxjs/Observable";
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';
import 'rxjs/add/observable/throw';

@Injectable()
export class CustomHttp extends Http {
	constructor(backend: ConnectionBackend, defaultOptions: RequestOptions){
		super(backend, defaultOptions);
	}

	get(url:string, options?: RequestOptionArgs): Observable<Response>{
		return super.get(appConfig.apUrl + url, this.addJwt(options)).catch(this.handleError);
	}

	post(url: string, body: string, options?: RequestOptionArgs): Observable<Response>{
		return super.post(appConfig.apiUrl + url, body, this.addJwt(options)).catch(this.handleError);
	}

	put(url: string, body:string, options?: RequestOptionsArgs): Observable<Response>{
		return super.put(appConfig.apiUrl + url, body, this.addJwt(options)).catch(this.handleError);
	}

	delete(url: string, options?: RequestOptionsArgs): Observable<Response>{
		return super.delete(appConfig.apiUrl + url, this.addJwt(options)).catch(this.handleError);
	}

	//private helper methods
	private addJwt(options?: RequestOptionsArgs): RequestOptionsArgs{
		//ensure request options and headers are not null
		options = options || new RequestOptions();
		options.headers = options.headers || new Headers();

		//add authorization header with jwt token
		let currentUser = JSON.parse(localStorage.getItem('currentUser'));
		if(currentUser && currentUser.token){
			options.header.append('Authorization', 'Bearer', + currentUser.token);
		}
		return options;
	}

	private handleError(error: any){
		if(error.status === 401){
			//401 unauthorized response so log user out of CLient
			window.location.href = '/login';

		}
		return Observable.throw(error._body);
	}
}

export function customHttpFactory(xhrBackend: XHRBackend, requestOptions: RequestOptions): Http{
	return new CustomHttp(xhrBackend, requestOptions);
}

export let customHttpProvider ={
	provide: Http,
	userFactory: customHttpFactory,
	deps: [XHRBackend, RequestOptions]
};