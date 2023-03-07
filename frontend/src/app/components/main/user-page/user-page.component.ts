import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {baseImgUrl, baseUrl, currentUserIdKey} from "../../../app.module";
import {ActivatedRoute, Router} from "@angular/router";
import {ErrorHandleService} from "../../../services/error-handle.service";
import {catchError} from "rxjs";

@Component({
  selector: 'app-user-page',
  templateUrl: './user-page.component.html',
  styleUrls: ['./user-page.component.css']
})
export class UserPageComponent implements OnInit {
  public userInfo: any = null

  public posts: any[] = []

  public isMe: boolean = true

  constructor(public httpClient: HttpClient,
              public activatedRoute: ActivatedRoute,
              public errorHandleService: ErrorHandleService,
              public router: Router) {
  }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe((params: any) => {
      let myId = localStorage.getItem(currentUserIdKey)
      if (myId == null) {
        this.router.navigate(['/login'])
        return
      }
      this.httpClient.get(`${baseUrl}/users/${params.username}`)
        .pipe(catchError(this.errorHandleService.handleError))
        .subscribe((data: any) => {
          if (data == null) {
            this.router.navigate(['/404'])
          } else {
            this.userInfo = data
            this.isMe = myId == this.userInfo.id
            this.userInfo.avatarFilename = `${baseImgUrl}${this.userInfo.avatarFilename}`
            this.userInfo.wallpaperFilename = `${baseImgUrl}${this.userInfo.wallpaperFilename}`
            this.httpClient.get(`${baseUrl}/posts`,
              {params: {authorId: this.userInfo.id, filtered: !this.isMe}})
              .pipe(catchError(this.errorHandleService.handleError))
              .subscribe((data: any) => {
                this.posts = data
              })
          }
        })
    })
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
      this.httpClient.get(`${baseUrl}/follow`,
        {params: {followeeId: this.userInfo.id}})
        .pipe(catchError(this.errorHandleService.handleError))
        .subscribe((data: any) => {
          if (data) {
            this.userInfo.followedByMe = true
          } else {
            alert('操作失败')
          }
        })
    }
  }
}
