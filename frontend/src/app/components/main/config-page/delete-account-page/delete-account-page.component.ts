import { Component, OnInit } from '@angular/core';
import {UserInfoService} from "../../../../services/user-info.service";
import {ErrorHandleService} from "../../../../services/error-handle.service";
import {catchError} from "rxjs";

@Component({
  selector: 'app-delete-account-page',
  templateUrl: './delete-account-page.component.html',
  styleUrls: ['./delete-account-page.component.css']
})
export class DeleteAccountPageComponent implements OnInit {

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

  onUserBarClick() {

  }

}
