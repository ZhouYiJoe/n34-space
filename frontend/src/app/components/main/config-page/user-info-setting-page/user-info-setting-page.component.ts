import { Component, OnInit } from '@angular/core';
import {UserInfoService} from "../../../../services/user-info.service";
import {catchError} from "rxjs";
import {ErrorHandleService} from "../../../../services/error-handle.service";

@Component({
  selector: 'app-user-info-setting-page',
  templateUrl: './user-info-setting-page.component.html',
  styleUrls: ['./user-info-setting-page.component.css']
})
export class UserInfoSettingPageComponent implements OnInit {

  public userInfo: any

  constructor(public userInfoService: UserInfoService,
              public errorHandleService: ErrorHandleService) { }

  ngOnInit(): void {
    this.userInfoService.getUserInfoRequest()
      .pipe(catchError(this.errorHandleService.handleError))
      .subscribe((userInfo: any) => {
        this.userInfo = userInfo
      })
  }

}
