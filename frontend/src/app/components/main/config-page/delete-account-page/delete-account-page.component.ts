import { Component, OnInit } from '@angular/core';
import {UserInfoService} from "../../../../services/user-info.service";
import {ErrorHandleService} from "../../../../services/error-handle.service";
import {catchError} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {baseUrl} from "../../../../app.module";
import {Router} from "@angular/router";
import {RegexService} from "../../../../services/regex.service";

@Component({
  selector: 'app-delete-account-page',
  templateUrl: './delete-account-page.component.html',
  styleUrls: ['./delete-account-page.component.css']
})
export class DeleteAccountPageComponent implements OnInit {

  public userInfo: any

  public inputBoxFocused: boolean = false

  public password: string = ''

  constructor(public userInfoService: UserInfoService,
              public errorHandleService: ErrorHandleService,
              public httpClient: HttpClient,
              public router: Router,
              public regexService: RegexService) { }

  ngOnInit(): void {
    this.userInfoService.getUserInfoRequest()
      .pipe(catchError(this.errorHandleService.handleError))
      .subscribe((userInfo: any) => {
        this.userInfo = userInfo
      })
  }

  onButtonClick() {
    if (!this.regexService.checkPassword(this.password)) {
      alert('密码格式非法')
      return
    }
    this.httpClient.post(`${baseUrl}/auth/deleteAccount`,
      {password: this.password})
      .pipe(catchError(this.errorHandleService.handleError))
      .subscribe((data: any) => {
        if (data) {
          alert('注销成功')
          this.router.navigate(['/login'])
        } else {
          alert('注销失败')
        }
      })
  }

}
