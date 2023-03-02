import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {baseUrl, currentFilterConfigKey} from "../../../app.module";
import {ErrorHandleService} from "../../../services/error-handle.service";
import {catchError} from "rxjs";
import {UserInfoService} from "../../../services/user-info.service";

@Component({
  selector: 'app-config-page',
  templateUrl: './config-page.component.html',
  styleUrls: ['./config-page.component.css']
})
export class ConfigPageComponent implements OnInit {
  public categories: string[] = []
  public filterConfig: boolean[] = []

  constructor(public httpClient: HttpClient,
              public errorHandleService: ErrorHandleService,
              public userInfoService: UserInfoService) {

  }

  ngOnInit(): void {
    let t = this.userInfoService.getUserInfo()?.filterConfig
    this.httpClient.get(`${baseUrl}/textCategories`)
      .pipe(catchError(this.errorHandleService.handleError))
      .subscribe((data: any) => {
        this.categories = data
        if (t !== undefined) {
          this.filterConfig = this.configStrToArray(t)
        }
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

  changeFilterConfig() {
    let strConfig = this.configArrayToStr(this.filterConfig)
    let userId = this.userInfoService.getUserInfo()?.userId
    this.httpClient.post(`${baseUrl}/users/filterConfig`,
      {filterConfig: strConfig, id: userId})
      .pipe(catchError(this.errorHandleService.handleError))
      .subscribe((data: any) => {
        if (data) {
          localStorage.setItem(currentFilterConfigKey, strConfig)
          alert('保存成功')
        } else {
          let strConfig = this.userInfoService.getUserInfo()?.filterConfig
          if (strConfig !== undefined) {
            this.filterConfig = this.configStrToArray(strConfig)
          }
        }
      })
  }

}
