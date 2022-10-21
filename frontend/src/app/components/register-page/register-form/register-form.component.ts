import {Component, OnInit} from '@angular/core';
import {RegexService} from "../../../services/regex.service";
import {baseApi, handleError} from "../../../app.module";
import {HttpClient} from "@angular/common/http";
import {catchError} from "rxjs";
import {Router} from "@angular/router";

@Component({
  selector: 'app-register-form',
  templateUrl: './register-form.component.html',
  styleUrls: ['./register-form.component.css']
})
export class RegisterFormComponent implements OnInit {
  public userInfo: any = {
    username: '',
    password: '',
    nickname: '',
    email: ''
  }

  constructor(public httpClient: HttpClient,
              public regexService: RegexService,
              public router: Router) {
  }

  ngOnInit(): void {
  }

  register(): void {
    if (!this.regexService.checkUsername(this.userInfo.username)) {
      alert('用户名必须是1到64位字母、数字或下划线')
      return
    }
    if (!this.regexService.checkPassword(this.userInfo.password)) {
      alert('密码必须是6到16位字母、数字或下划线')
      return
    }
    if (!this.regexService.checkNickname(this.userInfo.nickname)) {
      alert('昵称必须是1到64位非空白字符')
      return
    }
    if (!this.regexService.checkEmail(this.userInfo.email)) {
      alert('邮箱格式不正确')
      return
    }

    let url: string = `${baseApi}/auth/register`
    this.httpClient.post(url, this.userInfo)
      .pipe(catchError(handleError))
      .subscribe(data => {
        alert('注册成功，接下来跳转到登录页面')
        this.router.navigate(['/login'])
      })
  }
}
