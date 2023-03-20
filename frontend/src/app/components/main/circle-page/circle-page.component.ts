import {Component, OnInit, ViewChild} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {ErrorHandleService} from "../../../services/error-handle.service";
import {ActivatedRoute, Router} from "@angular/router";
import {UserInfoService} from "../../../services/user-info.service";
import {baseImgUrl, baseUrl} from "../../../app.module";
import {catchError} from "rxjs";

@Component({
  selector: 'app-circle-page',
  templateUrl: './circle-page.component.html',
  styleUrls: ['./circle-page.component.css']
})
export class CirclePageComponent implements OnInit {

  @ViewChild('avatarFileInput')
  public avatarFileInput: any

  @ViewChild('wallpaperFileInput')
  public wallpaperFileInput: any

  public circle: any = null

  public nameInput: string = ''

  public introductionInput: string = ''

  public posts: any = []

  public showEditWindow: boolean = false

  public myId: string = ''

  public menuOptionSelected: number = 1

  public postToPublishContent: string = ''

  constructor(public httpClient: HttpClient,
              public errorHandleService: ErrorHandleService,
              public router: Router,
              public userInfoService: UserInfoService,
              public activatedRoute: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.userInfoService.getUserInfoRequest()
      .pipe(catchError(this.errorHandleService.handleError))
      .subscribe((user: any) => {
        this.myId = user.id
      })
    this.activatedRoute.params.subscribe((params: any) => {
      this.httpClient.get(`${baseUrl}/circle/${params.circleId}`)
        .pipe(catchError(this.errorHandleService.handleError))
        .subscribe((data: any) => {
          this.circle = data
          this.circle.avatarFilename = `${baseImgUrl}${this.circle.avatarFilename}`
          this.circle.wallpaperFilename = `${baseImgUrl}${this.circle.wallpaperFilename}`
          this.nameInput = this.circle.name
          this.introductionInput = this.circle.introduction
          this.httpClient.get(`${baseUrl}/posts/hot`,
            {params: {circleId: this.circle.id, filtered: true}})
            .pipe(catchError(this.errorHandleService.handleError))
            .subscribe((data: any) => {
              this.posts = data
            })
        })
    })
  }

  publishPost(): void {
    this.httpClient.post(`${baseUrl}/posts`, {
      content: this.postToPublishContent,
      circleId: this.circle.id
    })
      .pipe(catchError(this.errorHandleService.handleError))
      .subscribe(data => {
        if (!data) {
          alert('推文发布失败，原因未知')
        } else {
          alert('发布成功！')
          this.postToPublishContent = ''
          location.reload()
        }
      })
  }

  closeEditWindow() {
    this.showEditWindow = false
    this.nameInput = this.circle.name
    this.introductionInput = this.circle.introduction
  }

  selectAvatar(): void {
    this.avatarFileInput.nativeElement.click()
  }

  uploadAvatar(): void {
    if (this.avatarFileInput.nativeElement.files.length == 0) {
      return
    }
    let formData = new FormData()
    formData.append('avatarFile', this.avatarFileInput.nativeElement.files[0])
    this.httpClient.post(`${baseUrl}/circle/${this.circle.id}/avatar`, formData, {responseType: 'text'})
      .pipe(catchError(this.errorHandleService.handleError))
      .subscribe((data: any) => {
        alert('上传成功')
        this.circle.avatarFilename = `${baseImgUrl}${data}`
        location.reload()
      })
  }

  selectWallpaper() {
    this.wallpaperFileInput.nativeElement.click()
  }

  uploadWallpaper() {
    if (this.wallpaperFileInput.nativeElement.files.length == 0) {
      return
    }
    let formData = new FormData()
    formData.append('wallpaperFile', this.wallpaperFileInput.nativeElement.files[0])
    this.httpClient.post(`${baseUrl}/circle/${this.circle.id}/wallpaper`, formData, {responseType: 'text'})
      .pipe(catchError(this.errorHandleService.handleError))
      .subscribe((data: any) => {
        alert('上传成功')
        this.circle.wallpaperFilename = `${baseImgUrl}${data}`
        location.reload()
      })
  }

  saveChange() {
    this.httpClient.put(`${baseUrl}/circle`,
      {
        id: this.myId,
        name: this.nameInput,
        introduction: this.introductionInput
      })
      .pipe(catchError(this.errorHandleService.handleError))
      .subscribe((data: any) => {
        if (data) {
          alert('保存成功')
          this.showEditWindow = false
          location.reload()
        } else {
          alert('保存失败')
        }
      })
  }

  deletePost(postId: string) {
    this.posts = this.posts.filter((post: any) => post.id != postId)
  }

  hotSelected() {
    this.menuOptionSelected = 1
    this.httpClient.get(`${baseUrl}/posts/hot`,
      {params: {circleId: this.circle.id}})
      .pipe(catchError(this.errorHandleService.handleError))
      .subscribe((data: any) => {
        this.posts = data
      })
  }

  latestSelected() {
    this.menuOptionSelected = 2
    this.httpClient.get(`${baseUrl}/posts/latest`,
      {params: {circleId: this.circle.id}})
      .pipe(catchError(this.errorHandleService.handleError))
      .subscribe((data: any) => {
        this.posts = data
      })
  }
}
