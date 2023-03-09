import {Component, OnInit} from '@angular/core';
import {RegexService} from "../../../../services/regex.service";
import {HttpClient} from "@angular/common/http";
import {baseUrl} from "../../../../app.module";
import {catchError} from "rxjs";
import {ErrorHandleService} from "../../../../services/error-handle.service";

@Component({
  selector: 'app-change-password-page',
  templateUrl: './change-password-page.component.html',
  styleUrls: ['./change-password-page.component.css']
})
export class ChangePasswordPageComponent implements OnInit {

  public inputBoxAttrs: any[] = [
    {
      fieldName: '当前密码',
      focused: false,
      content: ''
    },
    {
      fieldName: '新密码',
      focused: false,
      content: ''
    },
    {
      fieldName: '确认密码',
      focused: false,
      content: ''
    }
  ]

  constructor(public regexService: RegexService,
              public httpClient: HttpClient,
              public errorHandleService: ErrorHandleService) {
  }

  ngOnInit(): void {
  }

  save() {
    if (this.inputBoxAttrs[1].content != this.inputBoxAttrs[2].content) {
      alert('两次输入的新密码不一致')
      return
    }
    if (!this.regexService.checkPassword(this.inputBoxAttrs[0].content) ||
      !this.regexService.checkPassword(this.inputBoxAttrs[1].content)) {
      alert('密码格式非法')
      return
    }
    this.httpClient.post(`${baseUrl}/auth/changePassword`,
      {oldPassword: this.inputBoxAttrs[0].content, newPassword: this.inputBoxAttrs[1].content})
      .pipe(catchError(this.errorHandleService.handleError))
      .subscribe((data: any) => {
        if (data) {
          alert('修改成功')
        } else {
          alert('修改失败')
        }
      })
  }

}
