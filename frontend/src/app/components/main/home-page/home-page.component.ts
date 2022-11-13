import {Component, OnInit} from '@angular/core';
import {baseUrl} from "../../../app.module";
import {HttpClient} from "@angular/common/http";
import {catchError} from "rxjs";
import {ErrorHandleService} from "../../../services/error-handle.service";

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css']
})
export class HomePageComponent implements OnInit {
  public userInfo: any = {
    id: null,
    username: null,
    email: null,
    nickname: null
  }

  public posts: any[] = []

  constructor(public httpClient: HttpClient,
              public errorHandleService: ErrorHandleService) {
  }

  ngOnInit(): void {
    this.httpClient.get(`${baseUrl}/users/self`)
      .pipe(catchError(this.errorHandleService.handleError))
      .subscribe((data: any) => {
        this.userInfo.id = data.id
        this.userInfo.username = data.username
        this.userInfo.email = data.email
        this.userInfo.nickname = data.nickname
        this.httpClient.get(`${baseUrl}/posts`, {
          params: {
            authorId: data.id,
            pageNo: 0,
            pageSize: 5
          }
        })
          .pipe(catchError(this.errorHandleService.handleError))
          .subscribe((data: any) => {
            this.posts = data.records
          })
      })

  }
}
