import {Component, Input, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {baseUrl} from "../../../../app.module";
import {ErrorHandleService} from "../../../../services/error-handle.service";
import {catchError} from "rxjs";

@Component({
  selector: 'app-followee-list-item',
  templateUrl: './followee-list-item.component.html',
  styleUrls: ['./followee-list-item.component.css']
})
export class FolloweeListItemComponent implements OnInit {
  @Input()
  public followee: any

  constructor(public httpClient: HttpClient,
              public errorHandleService: ErrorHandleService) {
  }

  ngOnInit(): void {
  }

  onFollowButtonClick() {
    if (this.followee.followedByMe) {
      this.httpClient.delete(`${baseUrl}/follow`,
        {params: {followeeId: this.followee.id}})
        .pipe(catchError(this.errorHandleService.handleError))
        .subscribe((data: any) => {
          if (data) {
            this.followee.followedByMe = false
          } else {
            alert('操作失败')
          }
        })
    } else {
      this.httpClient.get(`${baseUrl}/follow`,
        {params: {followeeId: this.followee.id}})
        .pipe(catchError(this.errorHandleService.handleError))
        .subscribe((data: any) => {
          if (data) {
            this.followee.followedByMe = true
          } else {
            alert('操作失败')
          }
        })
    }
  }
}
