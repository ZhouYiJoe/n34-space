import { Component, OnInit } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {ErrorHandleService} from "../../../../services/error-handle.service";
import {UserInfoService} from "../../../../services/user-info.service";
import {catchError} from "rxjs";
import {baseUrl} from "../../../../app.module";

@Component({
  selector: 'app-filter-setting-page',
  templateUrl: './filter-setting-page.component.html',
  styleUrls: ['./filter-setting-page.component.css']
})
export class FilterSettingPageComponent implements OnInit {

  public categories: string[] = []
  public filterConfig: boolean[] = []

  constructor(public httpClient: HttpClient,
              public errorHandleService: ErrorHandleService,
              public userInfoService: UserInfoService) {

  }

  ngOnInit(): void {
    this.userInfoService.getUserInfoRequest()
      .pipe(catchError(this.errorHandleService.handleError))
      .subscribe((userInfo: any) => {
        let t = userInfo.filterConfig
        this.httpClient.get(`${baseUrl}/textCategories`)
          .pipe(catchError(this.errorHandleService.handleError))
          .subscribe((data: any) => {
            this.categories = data
            if (t !== undefined) {
              this.filterConfig = this.configStrToArray(t)
            }
          })
      })
  }

  configStrToArray(str: string): boolean[] {
    let array: boolean[] = []
    for (let i = 0; i < str.length; i++) {
      array.push(str[i] === '1')
    }
    return array
  }

  configArrayToStr(array: boolean[]): string {
    let t: string = ''
    for (let i = 0; i < array.length; i++) {
      t = t + (array[i] ? '1' : '0')
    }
    return t
  }

  changeFilterConfig(i: number) {
    this.filterConfig[i] = !this.filterConfig[i]
    this.userInfoService.getUserInfoRequest()
      .pipe(catchError(this.errorHandleService.handleError))
      .subscribe((userInfo: any) => {
        let strConfig = this.configArrayToStr(this.filterConfig)
        this.httpClient.post(`${baseUrl}/users/filterConfig`,
          {filterConfig: strConfig, id: userInfo.id})
          .pipe(catchError(this.errorHandleService.handleError))
          .subscribe((data: any) => {
            if (!data) {
              let strConfig = userInfo.filterConfig
              this.filterConfig = this.configStrToArray(strConfig)
            }
          })
      })

  }

}
