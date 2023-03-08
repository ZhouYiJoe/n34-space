import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Router} from "@angular/router";
import {baseUrl} from "../../../../../app.module";
import {catchError} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {ErrorHandleService} from "../../../../../services/error-handle.service";
import {UserInfoService} from "../../../../../services/user-info.service";

@Component({
  selector: 'app-comment-list-item',
  templateUrl: './comment-list-item.component.html',
  styleUrls: ['./comment-list-item.component.css']
})
export class CommentListItemComponent implements OnInit {
  @Input()
  public comment: any

  public showDeleteButton: boolean = false

  @Output()
  public deleted: EventEmitter<any> = new EventEmitter<any>()

  constructor(public router: Router,
              public httpClient: HttpClient,
              public errorHandleService: ErrorHandleService,
              public userInfoService: UserInfoService) {
  }

  ngOnInit(): void {
    this.userInfoService.getUserInfoRequest()
      .pipe(catchError(this.errorHandleService.handleError))
      .subscribe((userInfo: any) => {
        this.showDeleteButton = userInfo.id == this.comment.userId
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

  clickReplyButton() {
    this.router.navigate(['/app/comment/', this.comment.id])
  }

  deleteComment() {
    if (confirm('确定要删除吗？')) {
      this.httpClient.delete(`${baseUrl}/comments/${this.comment.id}`)
        .pipe(catchError(this.errorHandleService.handleError))
        .subscribe((data: any) => {
          if (data) {
            this.deleted.emit(this.comment.id)
          } else {
            alert('删除失败')
          }
        })
    }
  }
}
