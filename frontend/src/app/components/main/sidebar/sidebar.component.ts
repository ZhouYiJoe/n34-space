import { Component, OnInit } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {
  baseUrl, currentFilterConfigKey,
  currentUserAvatarFilenameKey,
  currentUserEmailKey,
  currentUserIdKey, currentUsernameKey,
  currentUserNicknameKey
} from "../../../app.module";
import {catchError} from "rxjs";
import {ErrorHandleService} from "../../../services/error-handle.service";

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent implements OnInit {
  public currentUserAvatarFilename: string | null = null
  public currentUsername: string | null = null
  public currentUserNickname: string | null = null
  public menuVisible: boolean = false

  constructor(public httpClient: HttpClient,
              public router: Router,
              public errorHandleService: ErrorHandleService) { }

  ngOnInit(): void {
    this.currentUserAvatarFilename = localStorage.getItem(currentUserAvatarFilenameKey)
    this.currentUsername = localStorage.getItem(currentUsernameKey)
    this.currentUserNickname = localStorage.getItem(currentUserNicknameKey)
    if (this.currentUserAvatarFilename === null || this.currentUsername === null || this.currentUserNickname === null) {
      this.router.navigate(['login'])
    }
  }

  logout() {
    this.httpClient.get(`${baseUrl}/auth/logout`)
      .pipe(catchError(this.errorHandleService.handleError))
      .subscribe((data: any) => {})
    localStorage.removeItem('token')
    localStorage.removeItem(currentUserIdKey)
    localStorage.removeItem(currentUserEmailKey)
    localStorage.removeItem(currentUserNicknameKey)
    localStorage.removeItem(currentUserAvatarFilenameKey)
    localStorage.removeItem(currentUsernameKey)
    localStorage.removeItem(currentFilterConfigKey)
    this.router.navigate(['login'])
  }

  onBlankClicked(event: any) {
    this.menuVisible = event.target.attributes.class && event.target.attributes.class.value === 'show-menu-area';
  }
}
