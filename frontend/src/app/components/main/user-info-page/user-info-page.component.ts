import {Component, OnInit, ViewChild} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {
  baseImgUrl,
  baseUrl, currentUserAvatarFilenameKey,
  currentUserEmailKey,
  currentUsernameKey,
  currentUserNicknameKey
} from "../../../app.module";
import {ErrorHandleService} from "../../../services/error-handle.service";
import {catchError} from "rxjs";
import {Router} from "@angular/router";

@Component({
  selector: 'app-user-info-page',
  templateUrl: './user-info-page.component.html',
  styleUrls: ['./user-info-page.component.css']
})
export class UserInfoPageComponent implements OnInit {
  @ViewChild('avatarFileInput')
  public avatarFileInput: any

  public avatarUrl: string | null = null

  public userInfo: any = {
    username: '',
    email: '',
    nickname: '',
    avatarFilename: ''
  }

  public editingNickname: boolean = false

  constructor(public httpClient: HttpClient,
              public errorHandleService: ErrorHandleService,
              public router: Router) {
  }

  ngOnInit(): void {
    this.userInfo.username = localStorage.getItem(currentUsernameKey)
    this.userInfo.email = localStorage.getItem(currentUserEmailKey)
    this.userInfo.nickname = localStorage.getItem(currentUserNicknameKey)
    this.userInfo.avatarFilename = localStorage.getItem(currentUserAvatarFilenameKey)
    if (this.userInfo.username === null ||
      this.userInfo.email === null ||
      this.userInfo.nickname === null ||
      this.userInfo.avatarFilename === null) {

      this.router.navigate(['/login'])
    }
    if (this.userInfo.avatarFilename !== 'null') {
      this.avatarUrl = `${baseImgUrl}${this.userInfo.avatarFilename}`
    }
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
          this.avatarUrl = `${baseImgUrl}${data}`
          localStorage.setItem(currentUserAvatarFilenameKey, data)
        }
      })
  }

  editNickname() {
    if (this.editingNickname) {
      this.httpClient.post(`${baseUrl}/users/nickname`, {
        username: this.userInfo.username,
        nickname: this.userInfo.nickname
      })
        .pipe(catchError((response) => {
          this.userInfo.nickname = localStorage.getItem(currentUserNicknameKey)
          return this.errorHandleService.handleError(response)
        }))
        .subscribe((data: any) => {
          if (!data) {
            alert('修改失败')
            this.userInfo.nickname = localStorage.getItem(currentUserNicknameKey)
          } else {
            localStorage.setItem(currentUserNicknameKey, this.userInfo.nickname)
          }
        })
    }
    this.editingNickname = !this.editingNickname
  }
}
