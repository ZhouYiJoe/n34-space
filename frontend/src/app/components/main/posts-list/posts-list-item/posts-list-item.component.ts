import {Component, Input, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {baseUrl, currentUserIdKey} from "../../../../app.module";
import {catchError} from "rxjs";
import {ErrorHandleService} from "../../../../services/error-handle.service";
import {Router} from "@angular/router";
import {UserInfoService} from "../../../../services/user-info.service";

@Component({
  selector: 'app-posts-list-item',
  templateUrl: './posts-list-item.component.html',
  styleUrls: ['./posts-list-item.component.css']
})
export class PostsListItemComponent implements OnInit {
  @Input()
  public post: any

  public menuVisible: boolean = false
  public editWindowVisible: boolean = false
  public commentWindowVisible: boolean = false

  public editInputBoxContent: string = ''
  public commentInputBoxContent: string = ''

  public curMaxNumCommentPage: number = 1
  public maxNumCommentPage: number = 0
  public comments: any[] = []

  @Input()
  public canEdit: boolean = false

  constructor(public hc: HttpClient,
              public eh: ErrorHandleService,
              public router: Router,
              public userInfoService: UserInfoService) {
  }

  ngOnInit(): void {
  }

  showMenu() {
    this.menuVisible = true
  }

  edit() {
    this.menuVisible = false
    this.editWindowVisible = true
    this.editInputBoxContent = this.post.content
  }

  delete() {
    this.hc.delete(`${baseUrl}/posts/${this.post.id}`)
      .pipe(catchError(this.eh.handleError))
      .subscribe((data: any) => {
        if (data) {
          this.post.deleted = true
        } else {
          alert('删除失败')
        }
      })
    this.menuVisible = false
  }

  submitUpdate() {
    this.hc.put(`${baseUrl}/posts`, {id: this.post.id, content: this.editInputBoxContent})
      .pipe(catchError(this.eh.handleError))
      .subscribe((data: any) => {
        if (data) {
          alert('修改成功')
          this.editWindowVisible = false
          this.post.content = this.editInputBoxContent
        } else {
          alert('修改失败')
        }
      })
  }

  cancelUpdate() {
    this.editWindowVisible = false
  }

  clickLikeButton(): void {
    if (this.post.likedByMe) {
      this.hc.delete(`${baseUrl}/post_likes`, {params: {postId: this.post.id}})
        .pipe(catchError(this.eh.handleError))
        .subscribe(data => {
        })
      this.post.numLike--
      this.post.likedByMe = false
    } else {
      this.hc.get(`${baseUrl}/post_likes`, {params: {postId: this.post.id}})
        .pipe(catchError(this.eh.handleError))
        .subscribe(data => {
        })
      this.post.numLike++
      this.post.likedByMe = true
    }
  }

  clickReplyButton() {
    this.commentWindowVisible = true
    this.refreshComments()
  }

  refreshComments() {
    this.hc.get(`${baseUrl}/comments`, {
      params: {
        postId: this.post.id,
        pageNo: 1,
        pageSize: 15
      }
    }).pipe(catchError(this.eh.handleError))
      .subscribe((data: any) => {
        this.comments = data.records
        this.curMaxNumCommentPage = 1
        this.maxNumCommentPage = data.pages
      })
  }

  closeCommentWindow() {
    this.commentWindowVisible = false
  }

  submitComment() {
    let currentUserId = this.userInfoService.getUserInfo()?.userId
    this.hc.post(`${baseUrl}/comments`, {
      content: this.commentInputBoxContent,
      userId: currentUserId,
      postId: this.post.id
    }).pipe(catchError(this.eh.handleError))
      .subscribe((data: any) => {
        if (data) {
          alert('发布成功')
          this.commentInputBoxContent = ''
          this.refreshComments()
          this.post.numComment++
        } else {
          alert('发布失败')
        }
      })
  }

  getNewCommentPage(event: any) {
    const {clientHeight, scrollHeight, scrollTop} = event.target
    const distanceToBottom = scrollHeight - (clientHeight + scrollTop)
    if (distanceToBottom >= 50) {
      return
    }
    if (this.maxNumCommentPage === 0) return
    if (this.curMaxNumCommentPage < this.maxNumCommentPage) {
      this.hc.get(`${baseUrl}/comments`, {
        params: {
          postId: this.post.id,
          pageNo: this.curMaxNumCommentPage + 1,
          pageSize: 15
        }
      }).pipe(catchError(this.eh.handleError))
        .subscribe((data: any) => {
          this.comments.push(...data.records)
          this.curMaxNumCommentPage++
        })
    }
  }
}
