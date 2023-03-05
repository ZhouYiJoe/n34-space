import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {baseUrl} from "../../../../app.module";
import {catchError} from "rxjs";
import {ErrorHandleService} from "../../../../services/error-handle.service";
import {Router} from "@angular/router";

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

  public editInputBoxContent: string = ''

  public comments: any[] = []

  @Input()
  public canEdit: boolean = false

  @Output()
  public deleted: EventEmitter<any> = new EventEmitter<any>()

  constructor(public hc: HttpClient,
              public eh: ErrorHandleService,
              public router: Router) {
  }

  ngOnInit(): void {
  }

  showOrHideMenu() {
    this.menuVisible = !this.menuVisible
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
          this.deleted.emit(this.post.id)
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
          this.post.html = data.html
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
    this.router.navigate(['app/post/', this.post.id])
  }

  onClickInPostContent(event: any) {
    if (!event.target.attributes.link) return
    if (!event.target.attributes.param) return
    let link = event.target.attributes.link.value
    let param = event.target.attributes.param.value
    this.router.navigate([link, param])
  }
}
