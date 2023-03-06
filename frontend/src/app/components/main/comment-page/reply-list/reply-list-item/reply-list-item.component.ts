import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {ErrorHandleService} from "../../../../../services/error-handle.service";
import {baseUrl, currentUserIdKey} from "../../../../../app.module";
import {catchError} from "rxjs";

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
              public errorHandleService: ErrorHandleService) {
  }

  ngOnInit(): void {
    let currentUserId = localStorage.getItem(currentUserIdKey);
    if (currentUserId === null) {
      this.router.navigate(['/login'])
      return
    }
    this.showDeleteButton = currentUserId == this.reply.userId
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
    let currentUserId = localStorage.getItem(currentUserIdKey)
    if (currentUserId == null) {
      this.router.navigate(['/login'])
      return
    }
    this.httpClient.post(`${baseUrl}/comment_replies`,
      {
        content: `回复@${this.reply.username}：` + this.replyInputBoxContent,
        userId: currentUserId,
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
