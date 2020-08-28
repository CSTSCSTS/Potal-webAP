import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Router } from "@angular/router";

class Login {
   username: string;
   password: string;
}


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
public login: Login;
public errorList: string[];

  constructor(private router:Router, private http:HttpClient) { }

  ngOnInit() {
    this.login = new Login();
    this.errorList = [];
  }

  onOAuthSubmit() {

    console.log('OAuth')

    const url = window.location + 'oauth-login';
    const body = new HttpParams()
    this.http
		  .post(url, body.toString(), {
		       headers: new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded')
		       })
		  .subscribe();

  }

  onSubmit() {
    console.log(window.location + '');
    console.log(this.login);
    console.log(this.login.username);
    console.log(this.login.password);
    this.NullOrEmptyCheck(this.errorList, this.login.username, 'ユーザー名が未入力です');
    this.NullOrEmptyCheck(this.errorList, this.login.password, 'パスワードが未入力です');

    if(this.errorList.length != 0) {
      return;
    }
    const url = window.location + 'login';

    const body = new HttpParams()
    .set('userName', this.login.username)
    .set('password', this.login.password);

    this.http
		  .post(url, body.toString(), {
		       headers: new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded')
		       })
		  .subscribe();

    this.router.navigate(["potal"])


    alert("ボタンが押されました");
  }

  NullOrEmptyCheck(errorList, value, errorMessage) {
		if(!(value)) {
			errorList.push(errorMessage);
		}
		return errorList;
	}


}
