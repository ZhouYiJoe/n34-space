import {Component, Input, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {ErrorHandleService} from "../../../../services/error-handle.service";
import {baseUrl} from "../../../../app.module";
import {catchError} from "rxjs";
import {Router} from "@angular/router";

@Component({
  selector: 'app-user-list-item',
  templateUrl: './user-list-item.component.html',
  styleUrls: ['./user-list-item.component.css']
})
export class UserListItemComponent implements OnInit {

  @Input()
  public user: any

  constructor(public httpClient: HttpClient,
              public errorHandleService: ErrorHandleService,
              public router: Router) {
  }

  ngOnInit(): void {
  }

  onFollowButtonClick() {
    if (this.user.followedByMe) {
      this.httpClient.delete(`${baseUrl}/follow`,
        {params: {followeeId: this.user.id}})
        .pipe(catchError(this.errorHandleService.handleError))
        .subscribe((data: any) => {
          if (data) {
            this.user.followedByMe = false
          } else {
            alert('操作失败')
          }
        })
    } else {
      this.httpClient.get(`${baseUrl}/follow`,
        {params: {followeeId: this.user.id}})
        .pipe(catchError(this.errorHandleService.handleError))
        .subscribe((data: any) => {
          if (data) {
            this.user.followedByMe = true
          } else {
            alert('操作失败')
          }
        })
    }
  }

  onClick(event: any) {
    if (event.target.id != 'follow-button') {
      this.router.navigate(['/app/users', this.user.username])
    }
  }
}
