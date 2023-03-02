import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {ErrorHandleService} from "../../../services/error-handle.service";
import {Router} from "@angular/router";
import {baseUrl} from "../../../app.module";
import {catchError} from "rxjs";
import {UserInfoService} from "../../../services/user-info.service";

@Component({
  selector: 'app-hot-page',
  templateUrl: './hot-page.component.html',
  styleUrls: ['./hot-page.component.css']
})
export class HotPageComponent implements OnInit {
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
    this.httpClient.get(`${baseUrl}/posts/hot`,
      {params: {searchText: ''}})
      .pipe(catchError(this.errorHandleService.handleError))
      .subscribe((data: any) => {
        this.posts = data
      })
  }
}
