import { Component, OnInit } from '@angular/core';
import {UserService} from '../user.service';
import {user} from  '../user';//true

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css'],
  providers:[UserService]
})
export class UserComponent implements OnInit {
	users:Users[];
	user:User;
	firstName:String;
	lastName:String;
	userName:String;
	email:String;
	passWord:String

  constructor(private userService:UserService) { }

  addUser()
  {
    const newUser = {
      firstName: this.firstName,
      lastName: this.lastName;
      userName: this.userName;
      email:this.email
      password:this.password;
    }

  }

  deleteUser(id:any)
  {
    var users = this.user;
    this.userService.deleteUser(id).subscribe(data =>{
      if(data.n==1)
      {
        for(var i = 0; i< users.length; i++){
          if(users[i]._id == id)
          {
            users.splice(i,1);
          }
        }
      }

    })
  }

  ngOnInit() {
  	this.userService.getUsers()
  		.subscribe(users => this.users = users);
  }

}
