import { Component, OnInit } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {RegexService} from "../../../services/regex.service";
import {baseApi, handleError} from "../../../app.module";
import {catchError} from "rxjs";
import {Router} from "@angular/router";
import {LocalStorageService} from "../../../services/local-storage.service";

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.css']
})
export class LoginFormComponent implements OnInit {
  public userInfo: any = {
    username: '',
    password: ''
  }

  constructor(public httpClient: HttpClient,
              public regexService: RegexService,
              public router: Router,
              public localStorageService: LocalStorageService) { }

  ngOnInit(): void {
  }

  login(): void {
    if (!this.regexService.checkUsername(this.userInfo.username)) {
      alert('用户名必须是1到64位字母、数字或下划线')
      return
    }
    if (!this.regexService.checkPassword(this.userInfo.password)) {
      alert('密码必须是6到16位字母、数字或下划线')
      return
    }

    let url: string = `${baseApi}/auth/login`
    let options: any = {responseType: 'text'}
    this.httpClient.post(url, this.userInfo, options)
      .pipe(catchError(handleError))
      .subscribe(data => {
        this.localStorageService.set('token', data)
        this.router.navigate(['/home'])
      })
  }
}
