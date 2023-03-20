import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {baseUrl} from "../../../app.module";
import {catchError} from "rxjs";
import {ErrorHandleService} from "../../../services/error-handle.service";
import {UserInfoService} from "../../../services/user-info.service";

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent implements OnInit {
  public myAvatarFilename: string | null = null
  public myUsername: string | null = null
  public myNickname: string | null = null
  public myId: string | null = null
  public menuVisible: boolean = false
  public numNotification: number = 0
  public numMessage: number = 0

  constructor(public httpClient: HttpClient,
              public router: Router,
              public errorHandleService: ErrorHandleService,
              public userInfoService: UserInfoService) { }

  ngOnInit(): void {
    this.userInfoService.getUserInfoRequest().subscribe((userInfo: any) => {
      this.myId = userInfo.id
      this.myAvatarFilename = userInfo.avatarFilename
      this.myUsername = userInfo.username
      this.myNickname = userInfo.nickname
    })
    this.httpClient.get(`${baseUrl}/mention_notification/countNewNotification`)
      .pipe(catchError(this.errorHandleService.handleError))
      .subscribe((numMentionNotification: any) => {
        this.numNotification += numMentionNotification
      })
    this.httpClient.get(`${baseUrl}/reply_notification/countNewNotification`)
      .pipe(catchError(this.errorHandleService.handleError))
      .subscribe((numReplyNotification: any) => {
        this.numNotification += numReplyNotification
      })
    this.httpClient.get(`${baseUrl}/invitation_notification/countNewNotification`)
      .pipe(catchError(this.errorHandleService.handleError))
      .subscribe((numInvitationNotification: any) => {
        this.numNotification += numInvitationNotification
      })
    this.httpClient.get(`${baseUrl}/message/countNewMessagesToMe`)
      .pipe(catchError(this.errorHandleService.handleError))
      .subscribe((numMessage: any) => {
        this.numMessage = numMessage
      })
  }

  logout() {
    this.httpClient.get(`${baseUrl}/auth/logout`)
      .pipe(catchError(this.errorHandleService.handleError))
      .subscribe((data: any) => {})
    localStorage.removeItem('token')
    this.router.navigate(['login'])
  }

  showOrHideUserBarMenu() {
    this.menuVisible = !this.menuVisible
  }
}
