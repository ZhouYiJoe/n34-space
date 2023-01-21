import { Component, OnInit } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {ErrorHandleService} from "../../../services/error-handle.service";
import {Router} from "@angular/router";
import {baseUrl, currentUserIdKey} from "../../../app.module";
import {catchError} from "rxjs";

@Component({
  selector: 'app-followee-posts-page',
  templateUrl: './followee-posts-page.component.html',
  styleUrls: ['./followee-posts-page.component.css']
})
export class FolloweePostsPageComponent implements OnInit {
  public posts: any[] = []

  public maxPageId: number | null = null

  public curMaxPageId: number = 1

  constructor(public httpClient: HttpClient,
              public errorHandleService: ErrorHandleService,
              public router: Router) {
  }

  ngOnInit(): void {
    this.refreshPosts()
  }

  ngAfterViewInit() {
    window.onscroll = () => {
      let clientHeight = document.documentElement.clientHeight || document.body.clientHeight
      let scrollHeight = document.documentElement.scrollHeight || document.body.scrollHeight
      let scrollTop = document.documentElement.scrollTop || document.body.scrollTop
      const distanceToBottom = scrollHeight - (clientHeight + scrollTop)
      if (distanceToBottom < 50) {
        this.getNextPage()
      }
    }
  }

  refreshPosts(): void {
    let currentUserId = localStorage.getItem(currentUserIdKey)
    if (currentUserId === null) {
      this.router.navigate(['/login'])
      return
    }
    this.httpClient.get(`${baseUrl}/posts/followee_posts`, {
      params: {
        authorId: currentUserId,
        pageNo: 1,
        pageSize: 15
      }
    }).pipe(catchError(this.errorHandleService.handleError))
      .subscribe((data: any) => {
        this.posts = data.records
        this.maxPageId = data.pages
        this.curMaxPageId = 1
      })
  }

  getNextPage(): void {
    if (this.maxPageId === null) return
    if (this.curMaxPageId < this.maxPageId) {
      let currentUserId = localStorage.getItem(currentUserIdKey)
      if (currentUserId === null) {
        this.router.navigate(['/login'])
        return
      }
      this.httpClient.get(`${baseUrl}/posts/followee_posts`, {
        params: {
          authorId: currentUserId,
          pageNo: this.curMaxPageId + 1,
          pageSize: 15
        }
      }).pipe(catchError(this.errorHandleService.handleError))
        .subscribe((data: any) => {
          this.posts.push(...data.records)
          this.curMaxPageId++
        })
    }
  }
}
