import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {ErrorHandleService} from "../../../services/error-handle.service";
import {baseUrl, currentUserIdKey} from "../../../app.module";
import {ActivatedRoute, NavigationExtras, Router} from "@angular/router";
import {catchError} from "rxjs";

@Component({
  selector: 'app-post-page',
  templateUrl: './post-page.component.html',
  styleUrls: ['./post-page.component.css']
})
export class PostPageComponent implements OnInit {
  public post: any = null

  public commentToPublish: any = {content: ''}

  public comments: any[] = []

  constructor(public httpClient: HttpClient,
              public activatedRoute: ActivatedRoute,
              public errorHandleService: ErrorHandleService,
              public router: Router) {
  }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe((params: any) => {
      this.httpClient.get(`${baseUrl}/posts/${params.postId}`)
        .pipe(catchError(this.errorHandleService.handleError))
        .subscribe((data: any) => {
          this.post = data
        })
    })
    this.activatedRoute.params.subscribe((params: any) => {
      this.httpClient.get(`${baseUrl}/comments`, {params: {postId: params.postId}})
        .pipe(catchError(this.errorHandleService.handleError))
        .subscribe((data: any) => {
          this.comments = data
        })
    })
  }

  onClickInPostContent(event: any) {
    if (!event.target.attributes.link) return
    if (!event.target.attributes.param) return
    let link = event.target.attributes.link.value
    let param = event.target.attributes.param.value
    this.router.navigate([link, param])
  }

  clickLikeButton(): void {
    if (this.post.likedByMe) {
      this.httpClient.delete(`${baseUrl}/post_likes`, {params: {postId: this.post.id}})
        .pipe(catchError(this.errorHandleService.handleError))
        .subscribe(data => {
        })
      this.post.numLike--
      this.post.likedByMe = false
    } else {
      this.httpClient.get(`${baseUrl}/post_likes`, {params: {postId: this.post.id}})
        .pipe(catchError(this.errorHandleService.handleError))
        .subscribe(data => {
        })
      this.post.numLike++
      this.post.likedByMe = true
    }
  }

  submitComment() {
    let currentUserId = localStorage.getItem(currentUserIdKey)
    this.httpClient.post(`${baseUrl}/comments`, {
      content: this.commentToPublish.content,
      userId: currentUserId,
      postId: this.post.id
    }).pipe(catchError(this.errorHandleService.handleError))
      .subscribe((data: any) => {
        if (data) {
          alert('发布成功')
          this.commentToPublish = ''
          this.refreshComments()
          this.post.numComment++
        } else {
          alert('发布失败')
        }
      })
  }

  refreshComments() {
    this.httpClient.get(`${baseUrl}/comments`, {params: {postId: this.post.id}})
      .pipe(catchError(this.errorHandleService.handleError))
      .subscribe((data: any) => {
        this.comments = data
      })
  }
}
