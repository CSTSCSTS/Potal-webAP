import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Router } from "@angular/router";

class User {
   username: string;
   password: string;
   passwordForCheck: string;
}


@Component({
  selector: 'app-user-register',
  templateUrl: './user-register.component.html',
  styleUrls: ['./user-register.component.css']
})
export class UserRegisterComponent implements OnInit {
  public user: User;
  public errorList: string[];
s
  constructor(private router:Router, private http:HttpClient) { }

  ngOnInit() {
    this.user = new User();
    this.errorList = [];
  }

  onSubmit() {
    console.log(window.location + '');
    this.NullOrEmptyCheck(this.errorList, this.user.username, 'ユーザー名が未入力です');
    this.NullOrEmptyCheck(this.errorList, this.user.password, 'パスワードが未入力です');
    this.NullOrEmptyCheck(this.errorList, this.user.passwordForCheck, 'パスワード(確認用)が未入力です');
    this.lengthOverCheck(this.errorList, this.user.username, 255, 'ユーザー名が255文字超えています');
    this.lengthOverCheck(this.errorList, this.user.password, 255, 'パスワードが255文字超えています');
    this.passwordSameCheck(this.errorList, this.user.password, this.user.passwordForCheck, 'パスワードとパスワード(確認)が一致していません。');

    if(this.errorList.length != 0) {
      return;
    }

    const url = window.location + '';

    console.log(this.user.username);
    console.log(this.user.password);

    const body = new HttpParams()
    .set('userName', this.user.username)
    .set('password', this.user.password);

    this.http
		  .post(url, body.toString(), {
		       headers: new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded')
		       })
		  .subscribe(

		    error => {

		    }
		  );

    this.router.navigate([""])
  }

  NullOrEmptyCheck(errorList, value, errorMessage) {
		if(!(value)) {
			errorList.push(errorMessage);
		}
		return errorList;
	}


  lengthOverCheck(errorList, value, limit, errorMessage) {
		if(value && value.length > limit) {
      errorList.push(errorMessage);
		}
		return errorList;
	}

	passwordSameCheck(errorList, password, confirmationPassword, errorMessage) {
    if(password !== confirmationPassword){
      errorList.push(errorMessage);
    }
    return errorList;
	}

}
