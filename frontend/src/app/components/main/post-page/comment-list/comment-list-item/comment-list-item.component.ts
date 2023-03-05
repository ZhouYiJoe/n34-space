import {Component, Input, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {baseUrl} from "../../../../../app.module";
import {catchError} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {ErrorHandleService} from "../../../../../services/error-handle.service";

@Component({
  selector: 'app-comment-list-item',
  templateUrl: './comment-list-item.component.html',
  styleUrls: ['./comment-list-item.component.css']
})
export class CommentListItemComponent implements OnInit {
  @Input()
  public comment: any

  constructor(public router: Router,
              public httpClient: HttpClient,
              public errorHandleService: ErrorHandleService) { }

  ngOnInit(): void {
    console.log(this.comment);
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
}
