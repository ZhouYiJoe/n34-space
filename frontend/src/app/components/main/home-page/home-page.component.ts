import {Component, OnInit} from '@angular/core';
import {baseUrl} from "../../../app.module";
import {HttpClient} from "@angular/common/http";
import {catchError} from "rxjs";
import {ErrorHandleService} from "../../../services/error-handle.service";
import {Router} from "@angular/router";
import {UserInfoService} from "../../../services/user-info.service";

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css']
})
export class HomePageComponent implements OnInit {
  public posts: any[] = []

  constructor(public httpClient: HttpClient,
              public errorHandleService: ErrorHandleService,
              public router: Router,
              public userInfoService: UserInfoService) {
  }

  ngOnInit(): void {
    this.refreshPosts()
  }

  ngAfterViewInit() {
  }

  refreshPosts(): void {
    this.userInfoService.getUserInfoRequest()
      .pipe(catchError(this.errorHandleService.handleError))
      .subscribe((userInfo: any) => {
        this.httpClient.get(`${baseUrl}/posts`, {
          params: {
            authorId: userInfo.id,
            filtered: false
          }
        }).pipe(catchError(this.errorHandleService.handleError))
          .subscribe((data: any) => {
            this.posts = data
          })
      })

  }
}
