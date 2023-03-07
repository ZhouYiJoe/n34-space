import {Component, OnInit, ViewChild} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {
  baseImgUrl,
  baseUrl, currentUserAvatarFilenameKey,
  currentUserNicknameKey
} from "../../../app.module";
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
    let formData = new FormData()
    formData.append('avatarFile', this.avatarFileInput.nativeElement.files[0])
    this.httpClient.post(`${baseUrl}/users/avatar`, formData, {responseType: 'text'})
      .pipe(catchError(this.errorHandleService.handleError))
      .subscribe((data: any) => {
        alert('上传成功')
        if (data != null) {
          this.userInfo.avatarFilename = `${baseImgUrl}${data}`
          localStorage.setItem(currentUserAvatarFilenameKey, data)
        }
      })
  }

  editNickname() {
    // if (this.editingNickname) {
    //   this.httpClient.post(`${baseUrl}/users/nickname`, {
    //     username: this.userInfo.username,
    //     nickname: this.userInfo.nickname
    //   })
    //     .pipe(catchError((response) => {
    //       this.userInfo.nickname = this.userInfoService.getUserInfo()?.nickname
    //       return this.errorHandleService.handleError(response)
    //     }))
    //     .subscribe((data: any) => {
    //       if (!data) {
    //         alert('修改失败')
    //         this.userInfo.nickname = this.userInfoService.getUserInfo()?.nickname
    //       } else {
    //         localStorage.setItem(currentUserNicknameKey, this.userInfo.nickname)
    //       }
    //     })
    // }
    // this.editingNickname = !this.editingNickname
  }
}
