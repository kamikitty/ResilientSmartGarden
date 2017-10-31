import { Injectable } from '@angular/core';
import {Http, Headers} from '@angular/http';
import {User} from './user';
import 'rxjs/add/operator/map';

@Injectable()
export class UserService {

  constructor(private http: Http) { }

  //retrieve contacts

  getUsers()
  {
  	return this.http.get('http://localhost:4000/user').map(res => res.json());
  }

  //add user
  addUser(newContact)
  {
  	var headers = new Headers();
  	headers.append('Content-Type', 'application/json');
  	return this.http.post('http://localhost:3000/api/user', newUser, {headers:headers}).map(res => res.json());
  }
  //delete method
  deleteContact(id)
  {
  	return this.http.delete('http://localhost:3000/api/contact/'+id).map(res=> res.json());
  }

}
