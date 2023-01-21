import { Component, OnInit } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {RegexService} from "../../../services/regex.service";
import {
  baseUrl, currentUserAvatarFilenameKey,
  currentUserEmailKey,
  currentUserIdKey,
  currentUsernameKey,
  currentUserNicknameKey
} from "../../../app.module";
import {catchError} from "rxjs";
import {Router} from "@angular/router";
import {ErrorHandleService} from "../../../services/error-handle.service";

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
              public errorHandleService: ErrorHandleService) { }

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

    let url: string = `${baseUrl}/auth/login`
    this.httpClient.post(url, this.userInfo, {responseType: 'text'})
      .pipe(catchError(this.errorHandleService.handleError))
      .subscribe(data => {
        localStorage.setItem('token', data)
        this.httpClient.get(`${baseUrl}/users/self`)
          .pipe(catchError(this.errorHandleService.handleError))
          .subscribe((data: any) => {
            localStorage.setItem(currentUserIdKey, data.id)
            localStorage.setItem(currentUsernameKey, data.username)
            localStorage.setItem(currentUserEmailKey, data.email)
            localStorage.setItem(currentUserNicknameKey, data.nickname)
            localStorage.setItem(currentUserAvatarFilenameKey, data.avatarFilename)
            this.router.navigate(['/app/home'])
          })
      })
  }
}
