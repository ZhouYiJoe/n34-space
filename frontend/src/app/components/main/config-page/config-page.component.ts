import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {ErrorHandleService} from "../../../services/error-handle.service";
import {UserInfoService} from "../../../services/user-info.service";

@Component({
  selector: 'app-config-page',
  templateUrl: './config-page.component.html',
  styleUrls: ['./config-page.component.css']
})
export class ConfigPageComponent implements OnInit {

  public settingOptions: any[] = [
    {
      name: '账号信息',
      selected: false,
      desc: '查看你的账号信息，例如邮箱地址和注册时间。'
    },
    {
      name: '更改密码',
      selected: false,
      desc: '随时更改你的密码。'
    },
    {
      name: '注销账号',
      selected: false,
      desc: '了解如何停用账号。'
    },
    {
      name: '内容过滤',
      selected: false,
      desc: '选择你想查看以及不想查看的推文。'
    }
  ]

  constructor(public httpClient: HttpClient,
              public errorHandleService: ErrorHandleService,
              public userInfoService: UserInfoService) {

  }

  ngOnInit() {
  }

  select(settingOption: any) {
    for (let i = 0; i < this.settingOptions.length; i++) {
      this.settingOptions[i].selected = this.settingOptions[i] === settingOption
    }
  }

}
