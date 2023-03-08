import {Component, OnInit, ViewChild} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {baseImgUrl, baseUrl} from "../../../app.module";
import {ErrorHandleService} from "../../../services/error-handle.service";
import {catchError} from "rxjs";
import {Router} from "@angular/router";
import {UserInfoService} from "../../../services/user-info.service";

@Component({
  selector: 'app-user-info-page',
  templateUrl: './user-info-page.component.html',
  styleUrls: ['./user-info-page.component.css']
})
export class UserInfoPageComponent implements OnInit {
  @ViewChild('avatarFileInput')
  public avatarFileInput: any

  @ViewChild('wallpaperFileInput')
  public wallpaperFileInput: any

  public userInfo: any = null

  public nicknameInput: string = ''

  public introductionInput: string = ''

  public locationInput: string = ''

  public linkInput: string = ''

  public posts: any = []

  public showEditWindow: boolean = false

  constructor(public httpClient: HttpClient,
              public errorHandleService: ErrorHandleService,
              public router: Router,
              public userInfoService: UserInfoService) {
  }

  ngOnInit(): void {
    this.httpClient.get(`${baseUrl}/users/self`)
      .pipe(catchError(this.errorHandleService.handleError))
      .subscribe((data: any) => {
        this.userInfo = data
        this.userInfo.avatarFilename = `${baseImgUrl}${this.userInfo.avatarFilename}`
        this.userInfo.wallpaperFilename = `${baseImgUrl}${this.userInfo.wallpaperFilename}`
        this.nicknameInput = this.userInfo.nickname
        this.introductionInput = this.userInfo.introduction
        this.locationInput = this.userInfo.location
        this.linkInput = this.userInfo.link
        this.httpClient.get(`${baseUrl}/posts`,
          {params: {authorId: this.userInfo.id, filtered: false}})
          .pipe(catchError(this.errorHandleService.handleError))
          .subscribe((data: any) => {
            this.posts = data
          })
      })
  }

  closeEditWindow() {
    this.showEditWindow = false
    this.nicknameInput = this.userInfo.nickname
    this.introductionInput = this.userInfo.introduction
    this.locationInput = this.userInfo.location
    this.linkInput = this.userInfo.link
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
    this.httpClient.post(`${baseUrl}/users/avatar`, formData, {responseType: 'text'})
      .pipe(catchError(this.errorHandleService.handleError))
      .subscribe((data: any) => {
        alert('上传成功')
        this.userInfo.avatarFilename = `${baseImgUrl}${data}`
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
    this.httpClient.post(`${baseUrl}/users/wallpaper`, formData, {responseType: 'text'})
      .pipe(catchError(this.errorHandleService.handleError))
      .subscribe((data: any) => {
        alert('上传成功')
        this.userInfo.wallpaperFilename = `${baseImgUrl}${data}`
        location.reload()
      })
  }

  saveChange() {
    this.userInfoService.getUserInfoRequest()
      .pipe(catchError(this.errorHandleService.handleError))
      .subscribe((userInfo: any) => {
        this.httpClient.put(`${baseUrl}/users`,
          {
            id: userInfo.id,
            nickname: this.nicknameInput,
            introduction: this.introductionInput,
            location: this.locationInput,
            link: this.linkInput
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
      })
  }
}
