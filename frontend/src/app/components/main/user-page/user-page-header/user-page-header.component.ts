import {Component, Input, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {baseUrl} from "../../../../app.module";
import {catchError} from "rxjs";
import {ErrorHandleService} from "../../../../services/error-handle.service";

@Component({
  selector: 'app-user-page-header',
  templateUrl: './user-page-header.component.html',
  styleUrls: ['./user-page-header.component.css']
})
export class UserPageHeaderComponent implements OnInit {
  @Input()
  public userInfo: any

  @Input()
  public followButtonShowed: boolean = false

  constructor(public httpClient: HttpClient,
              public errorHandleService: ErrorHandleService) {
  }

  ngOnInit(): void {
  }

  onFollowButtonClick() {
    if (this.userInfo.followedByMe) {
      this.httpClient.delete(`${baseUrl}/follow`,
        {params: {followeeId: this.userInfo.id}})
        .pipe(catchError(this.errorHandleService.handleError))
        .subscribe((data: any) => {
          if (data) {
            this.userInfo.followedByMe = false
          } else {
            alert('操作失败')
          }
        })
    } else {
      this.httpClient.get(`${baseUrl}/follow`, {params: {followeeId: this.userInfo.id}})
        .pipe(catchError(this.errorHandleService.handleError))
        .subscribe((data: any) => {
          if (data) {
            this.userInfo.followedByMe = true
          } else {
            alert('操作失败')
          }
        });
    }
  }
}
