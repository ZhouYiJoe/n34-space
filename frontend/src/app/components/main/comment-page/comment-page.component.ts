import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {ActivatedRoute, Router} from "@angular/router";
import {ErrorHandleService} from "../../../services/error-handle.service";
import {baseUrl, currentUserIdKey} from "../../../app.module";
import {catchError} from "rxjs";

@Component({
  selector: 'app-comment-page',
  templateUrl: './comment-page.component.html',
  styleUrls: ['./comment-page.component.css']
})
export class CommentPageComponent implements OnInit {

  public comment: any = null

  public replyToPublish: any = {content: ''}

  public replies: any[] = []

  constructor(public httpClient: HttpClient,
              public activatedRoute: ActivatedRoute,
              public errorHandleService: ErrorHandleService,
              public router: Router) {
  }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe((params: any) => {
      this.httpClient.get(`${baseUrl}/comments/${params.commentId}`)
        .pipe(catchError(this.errorHandleService.handleError))
        .subscribe((data: any) => {
          this.comment = data
        })
    })
    this.activatedRoute.params.subscribe((params: any) => {
      this.httpClient.get(`${baseUrl}/comment_replies`,
        {params: {commentId: params.commentId, timeSortOrder: 'asc'}})
        .pipe(catchError(this.errorHandleService.handleError))
        .subscribe((data: any) => {
          this.replies = data
        })
    })
  }

  onClickInCommentContent(event: any) {
    if (!event.target.attributes.link) return
    if (!event.target.attributes.param) return
    let link = event.target.attributes.link.value
    let param = event.target.attributes.param.value
    this.router.navigate([link, param])
  }

  clickLikeButton(): void {
    if (this.comment.likedByMe) {
      this.httpClient.delete(`${baseUrl}/comment_likes`, {params: {commentId: this.comment.id}})
        .pipe(catchError(this.errorHandleService.handleError))
        .subscribe(data => {
        })
      this.comment.numLike--
      this.comment.likedByMe = false
    } else {
      this.httpClient.get(`${baseUrl}/comment_likes`, {params: {commentId: this.comment.id}})
        .pipe(catchError(this.errorHandleService.handleError))
        .subscribe(data => {
        })
      this.comment.numLike++
      this.comment.likedByMe = true
    }
  }

  submitReply() {
    let currentUserId = localStorage.getItem(currentUserIdKey)
    this.httpClient.post(`${baseUrl}/comment_replies`, {
      content: this.replyToPublish.content,
      userId: currentUserId,
      commentId: this.comment.id
    }).pipe(catchError(this.errorHandleService.handleError))
      .subscribe((data: any) => {
        if (data) {
          alert('发布成功')
          this.replyToPublish.content = ''
          this.refreshReplies()
          this.comment.numReply++
        } else {
          alert('发布失败')
        }
      })
  }

  refreshReplies() {
    this.httpClient.get(`${baseUrl}/comment_replies`,
      {params: {commentId: this.comment.id, timeSortOrder: 'asc'}})
      .pipe(catchError(this.errorHandleService.handleError))
      .subscribe((data: any) => {
        this.replies = data
        this.comment.numReply = this.replies.length
      })
  }

}
