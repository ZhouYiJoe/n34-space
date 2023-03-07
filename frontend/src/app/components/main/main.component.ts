import {Component, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute, NavigationExtras, Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {ErrorHandleService} from "../../services/error-handle.service";
import {baseUrl, currentUserIdKey} from "../../app.module";
import {catchError} from "rxjs";

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent implements OnInit {
  @ViewChild('content')
  public content: any

  public searchText: string = ''

  public hotHashtags: any[] = []

  public hotUsers: any[] = []

  public myId: string | null = null

  constructor(public httpClient: HttpClient,
              public activatedRoute: ActivatedRoute,
              public errorHandleService: ErrorHandleService,
              public router: Router) {

  }

  ngOnInit(): void {
    this.myId = localStorage.getItem(currentUserIdKey)
    if (this.myId == null) {
      this.router.navigate(['/login'])
      return
    }
    this.httpClient.get(`${baseUrl}/hashtag/top`, {params: {n: 10}})
      .pipe(catchError(this.errorHandleService.handleError))
      .subscribe((data: any) => {
        this.hotHashtags = data
      })
    this.httpClient.get(`${baseUrl}/users`, {params: {sortByFollower: true, topN: 5, includeFollowed: false}})
      .pipe(catchError(this.errorHandleService.handleError))
      .subscribe((data: any) => {
        this.hotUsers = data.filter((user: any) => user.id != this.myId)
      })
  }

  search(event: KeyboardEvent) {
    if (event.key == 'Enter' && this.searchText) {
      let navigationExtras: NavigationExtras = {queryParams: {q: this.searchText}}
      this.router.navigate(['/app/search/hot'], navigationExtras)
    }
  }

  onFollowButtonClick(user: any) {
    if (user.followedByMe) {
      this.httpClient.delete(`${baseUrl}/follow`,
        {params: {followeeId: user.id}})
        .pipe(catchError(this.errorHandleService.handleError))
        .subscribe((data: any) => {
          if (data) {
            user.followedByMe = false
          } else {
            alert('操作失败')
          }
        })
    } else {
      this.httpClient.get(`${baseUrl}/follow`,
        {params: {followeeId: user.id}})
        .pipe(catchError(this.errorHandleService.handleError))
        .subscribe((data: any) => {
          if (data) {
            user.followedByMe = true
          } else {
            alert('操作失败')
          }
        })
    }
  }
}
