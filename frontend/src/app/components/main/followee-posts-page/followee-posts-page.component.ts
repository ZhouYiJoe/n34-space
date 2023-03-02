import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {ErrorHandleService} from "../../../services/error-handle.service";
import {Router} from "@angular/router";
import {baseUrl} from "../../../app.module";
import {catchError} from "rxjs";
import {UserInfoService} from "../../../services/user-info.service";

@Component({
  selector: 'app-followee-posts-page',
  templateUrl: './followee-posts-page.component.html',
  styleUrls: ['./followee-posts-page.component.css']
})
export class FolloweePostsPageComponent implements OnInit {
  public posts: any[] = []

  constructor(public httpClient: HttpClient,
              public errorHandleService: ErrorHandleService,
              public router: Router,
              public userInfoService: UserInfoService) {
  }

  ngOnInit(): void {
    this.refreshPosts()
  }

  refreshPosts(): void {
    let currentUserId = this.userInfoService.getUserInfo()?.userId
    if (currentUserId === undefined) return
    this.httpClient.get(`${baseUrl}/posts/followee_posts`,)
      .pipe(catchError(this.errorHandleService.handleError))
      .subscribe((data: any) => {
        this.posts = data
      })
  }
}
