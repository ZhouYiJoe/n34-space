import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {ErrorHandleService} from "../../../../../services/error-handle.service";
import {baseUrl} from "../../../../../app.module";
import {catchError} from "rxjs";
import {UserInfoService} from "../../../../../services/user-info.service";

@Component({
  selector: 'app-reply-list-item',
  templateUrl: './reply-list-item.component.html',
  styleUrls: ['./reply-list-item.component.css']
})
export class ReplyListItemComponent implements OnInit {

  @Input()
  public reply: any

  public showDeleteButton: boolean = false

  @Output()
  public deleted: EventEmitter<any> = new EventEmitter<any>()

  @Output()
  public replied: EventEmitter<any> = new EventEmitter<any>()

  public replyInputBoxContent: string = ''

  public replyInputWindowVisible: boolean = false

  constructor(public router: Router,
              public httpClient: HttpClient,
              public errorHandleService: ErrorHandleService,
              public userInfoService: UserInfoService) {
  }

  ngOnInit(): void {
    this.userInfoService.getUserInfoRequest()
      .pipe(catchError(this.errorHandleService.handleError))
      .subscribe((userInfo: any) => {
        this.showDeleteButton = userInfo.id == this.reply.userId
      })
  }

  onClickInReplyContent(event: any) {
    if (!event.target.attributes.link) return
    if (!event.target.attributes.param) return
    let link = event.target.attributes.link.value
    let param = event.target.attributes.param.value
    this.router.navigate([link, param])
  }

  clickLikeButton(): void {
    if (this.reply.likedByMe) {
      this.httpClient.delete(`${baseUrl}/comment_reply_likes`, {params: {commentReplyId: this.reply.id}})
        .pipe(catchError(this.errorHandleService.handleError))
        .subscribe(data => {
        })
      this.reply.numLike--
      this.reply.likedByMe = false
    } else {
      this.httpClient.get(`${baseUrl}/comment_reply_likes`, {params: {commentReplyId: this.reply.id}})
        .pipe(catchError(this.errorHandleService.handleError))
        .subscribe(data => {
        })
      this.reply.numLike++
      this.reply.likedByMe = true
    }
  }

  clickReplyButton() {
    this.replyInputWindowVisible = true
  }

  submitReply() {
    this.userInfoService.getUserInfoRequest()
      .pipe(catchError(this.errorHandleService.handleError))
      .subscribe((userInfo: any) => {
        this.httpClient.post(`${baseUrl}/comment_replies`,
          {
            content: `回复@${this.reply.username} : ` + this.replyInputBoxContent,
            userId: userInfo.id,
            commentId: this.reply.commentId
          })
          .pipe(catchError(this.errorHandleService.handleError))
          .subscribe((data: any) => {
            if (data) {
              alert('发布成功')
              this.replyInputWindowVisible = false
              this.replied.emit()
            } else {
              alert('发布失败')
            }
          })
      })

  }

  closeReplyInputWindow() {
    this.replyInputWindowVisible = false
  }

  deleteReply() {
    if (confirm('确定要删除吗？')) {
      this.httpClient.delete(`${baseUrl}/comment_replies/${this.reply.id}`)
        .pipe(catchError(this.errorHandleService.handleError))
        .subscribe((data: any) => {
          if (data) {
            this.deleted.emit(this.reply.id)
          } else {
            alert('删除失败')
          }
        })
    }
  }

}
