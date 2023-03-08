import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-change-password-page',
  templateUrl: './change-password-page.component.html',
  styleUrls: ['./change-password-page.component.css']
})
export class ChangePasswordPageComponent implements OnInit {

  public inputBoxAttrs: any[] = [
    {
      fieldName: '当前密码',
      focused: false
    },
    {
      fieldName: '新密码',
      focused: false
    },
    {
      fieldName: '确认密码',
      focused: false
    }
  ]

  constructor() { }

  ngOnInit(): void {
  }

}
