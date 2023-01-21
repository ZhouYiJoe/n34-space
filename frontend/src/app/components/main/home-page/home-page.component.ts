import {Component, OnInit} from '@angular/core';
import {baseImgUrl, baseUrl, currentUserIdKey} from "../../../app.module";
import {HttpClient} from "@angular/common/http";
import {catchError} from "rxjs";
import {ErrorHandleService} from "../../../services/error-handle.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css']
})
export class HomePageComponent implements OnInit {
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
    this.httpClient.get(`${baseUrl}/posts`, {
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
      this.httpClient.get(`${baseUrl}/posts`, {
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
