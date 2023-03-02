import { Injectable } from '@angular/core';
import {
  currentFilterConfigKey,
  currentUserAvatarFilenameKey,
  currentUserEmailKey,
  currentUserIdKey,
  currentUsernameKey,
  currentUserNicknameKey
} from "../app.module";
import {Router} from "@angular/router";

@Injectable({
  providedIn: 'root'
})
export class UserInfoService {

  constructor(public router: Router) { }

  public getUserInfo() {
    let userId = localStorage.getItem(currentUserIdKey)
    if (userId === null) {
      this.router.navigate(['/login'])
      return null
    }
    let username = localStorage.getItem(currentUsernameKey)
    if (username === null) {
      this.router.navigate(['/login'])
      return null
    }
    let email = localStorage.getItem(currentUserEmailKey)
    if (email === null) {
      this.router.navigate(['/login'])
      return null
    }
    let nickname = localStorage.getItem(currentUserNicknameKey)
    if (nickname === null) {
      this.router.navigate(['/login'])
      return null
    }
    let avatarFilename = localStorage.getItem(currentUserAvatarFilenameKey)
    if (avatarFilename === null) {
      this.router.navigate(['/login'])
      return null
    }
    let filterConfig = localStorage.getItem(currentFilterConfigKey)
    if (filterConfig === null) {
      this.router.navigate(['/login'])
      return null
    }
    return {
      userId: userId,
      username: username,
      email: email,
      nickname: nickname,
      avatarFilename: avatarFilename,
      filterConfig: filterConfig
    }
  }
}
