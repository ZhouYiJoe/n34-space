import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {baseUrl} from "../../../app.module";
import {ActivatedRoute, Router} from "@angular/router";
import {ErrorHandleService} from "../../../services/error-handle.service";
import {catchError} from "rxjs";
import {UserInfoService} from "../../../services/user-info.service";

@Component({
  selector: 'app-user-page',
  templateUrl: './user-page.component.html',
  styleUrls: ['./user-page.component.css']
})
export class UserPageComponent implements OnInit {
  public userInfo: any = {
    id: '',
    username: '',
    nickname: '',
    avatarFilename: '',
    followedByMe: false
  }

  public posts: any[] = []

  public maxPageId: number | null = null
  public curMaxPageId: number = 1

  public followButtonShowed: boolean = false

  constructor(public httpClient: HttpClient,
              public activatedRoute: ActivatedRoute,
              public errorHandleService: ErrorHandleService,
              public router: Router,
              public userInfoService: UserInfoService) {
  }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe((params: any) => {
      this.httpClient.get(`${baseUrl}/users/${params.username}`)
        .pipe(catchError(this.errorHandleService.handleError))
        .subscribe((data: any) => {
          if (data === null) {
            this.router.navigate(['/404'])
          } else {
            this.userInfo.username = data.username
            this.userInfo.nickname = data.nickname
            this.userInfo.avatarFilename = data.avatarFilename
            this.userInfo.id = data.id
            this.userInfo.followedByMe = data.followedByMe
            this.followButtonShowed = this.userInfo.id !== this.userInfoService.getUserInfo()?.userId
            this.refreshPosts()
          }
        })
    })
  }

  refreshPosts(): void {
    this.httpClient.get(`${baseUrl}/posts`, {
      params: {
        authorId: this.userInfo.id,
        filtered: true
      }
    }).pipe(catchError(this.errorHandleService.handleError))
      .subscribe((data: any) => {
        this.posts = data
      })
  }
}
