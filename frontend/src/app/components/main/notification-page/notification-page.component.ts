import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {baseUrl} from "../../../app.module";
import {catchError} from "rxjs";
import {ErrorHandleService} from "../../../services/error-handle.service";
import {Router} from "@angular/router";
import {UserInfoService} from "../../../services/user-info.service";

@Component({
  selector: 'app-notification-page',
  templateUrl: './notification-page.component.html',
  styleUrls: ['./notification-page.component.css']
})
export class NotificationPageComponent implements OnInit {
  public selectedIndex: number = 0
  public mentionNotifications: any[] = []
  public replyNotifications: any[] = []
  public invitationNotifications: any[] = []
  public numMentionNotification: number = 0
  public numReplyNotification: number = 0
  public numInvitationNotification: number = 0
  public myId: string = ''

  constructor(public httpClient: HttpClient,
              public errorHandleService: ErrorHandleService,
              public router: Router,
              public userInfoService: UserInfoService) { }

  ngOnInit(): void {
    this.replyNaviSelected()
    this.userInfoService.getUserInfoRequest()
      .pipe(catchError(this.errorHandleService.handleError))
      .subscribe((user: any) => {
        this.myId = user.id
      })
    this.httpClient.get(`${baseUrl}/mention_notification/countNewNotification`)
      .pipe(catchError(this.errorHandleService.handleError))
      .subscribe((numMentionNotification: any) => {
        this.numMentionNotification = numMentionNotification
      })
    this.httpClient.get(`${baseUrl}/reply_notification/countNewNotification`)
      .pipe(catchError(this.errorHandleService.handleError))
      .subscribe((numReplyNotification: any) => {
        this.numReplyNotification = numReplyNotification
      })
    this.httpClient.get(`${baseUrl}/invitation_notification/countNewNotification`)
      .pipe(catchError(this.errorHandleService.handleError))
      .subscribe((numInvitationNotification: any) => {
        this.numInvitationNotification = numInvitationNotification
      })
  }

  replyNaviSelected() {
    this.selectedIndex = 1
    this.httpClient.get(`${baseUrl}/reply_notification/getNewNotification`)
      .pipe(catchError(this.errorHandleService.handleError))
      .subscribe((data: any) => {
        this.replyNotifications = data
      })
  }

  mentionNaviSelected() {
    this.selectedIndex = 2
    this.httpClient.get(`${baseUrl}/mention_notification/getNewNotification`)
      .pipe(catchError(this.errorHandleService.handleError))
      .subscribe((data: any) => {
        this.mentionNotifications = data
      })
  }

  invitationNaviSelected() {
    this.selectedIndex = 3
    this.httpClient.get(`${baseUrl}/invitation_notification/getNewNotification`)
      .pipe(catchError(this.errorHandleService.handleError))
      .subscribe((data: any) => {
        this.invitationNotifications = data
      })
  }

  jumpToMention(mentionNotification: any) {
    if (mentionNotification.type == 'post') {
      this.router.navigate(['/app/post/', mentionNotification.jumpToTextId])
    } else if (mentionNotification.type == 'comment') {
      this.router.navigate(['/app/comment/', mentionNotification.jumpToTextId])
    } else if (mentionNotification.type == 'reply') {
      this.router.navigate(['/app/comment/', mentionNotification.jumpToTextId])
    }
  }

  jumpToRepliedText(replyNotification: any) {
    if (replyNotification.type == 'comment') {
      this.router.navigate(['/app/post/', replyNotification.repliedTextId])
    } else if (replyNotification.type == 'reply') {
      this.router.navigate(['/app/comment/', replyNotification.repliedTextId])
    }
  }

  jumpToReplyText(replyNotification: any) {
    if (replyNotification.type == 'comment') {
      this.router.navigate(['/app/comment/', replyNotification.replyTextId])
    } else if (replyNotification.type == 'reply') {
      this.router.navigate(['/app/comment/', replyNotification.repliedTextId])
    }
  }

  onClickInMentionNotification(event: any, mentionNotification: any) {
    if (!event.target.attributes.link || !event.target.attributes.param) {
      this.jumpToMention(mentionNotification)
      return
    }
    let link = event.target.attributes.link.value
    let param = event.target.attributes.param.value
    this.router.navigate([link, param])
  }

  onClickInRepliedText(event: any, replyNotification: any) {
    if (!event.target.attributes.link || !event.target.attributes.param) {
      this.jumpToRepliedText(replyNotification)
      return
    }
    let link = event.target.attributes.link.value
    let param = event.target.attributes.param.value
    this.router.navigate([link, param])
  }

  onClickInReplyText(event: any, replyNotification: any) {
    if (!event.target.attributes.link || !event.target.attributes.param) {
      this.jumpToReplyText(replyNotification)
      return
    }
    let link = event.target.attributes.link.value
    let param = event.target.attributes.param.value
    this.router.navigate([link, param])
  }

  acceptInvitation(invitationNotification: any) {
    if (invitationNotification.state == 'waiting') {
      this.httpClient.get(`${baseUrl}/circle/newMembership`, {
        params: {
          circleId: invitationNotification.circleId,
          memberId: this.myId
        }
      }).pipe(catchError(this.errorHandleService.handleError))
        .subscribe((data: any) => {
          this.httpClient.put(`${baseUrl}/invitation_notification`, {
            id: invitationNotification.id,
            state: 'accepted'
          }).pipe(catchError(this.errorHandleService.handleError))
            .subscribe((data: any) => {
              invitationNotification.state = 'accepted'
            })
        })
    }
  }

  rejectInvitation(invitationNotification: any) {
    if (invitationNotification.state == 'waiting') {
      this.httpClient.put(`${baseUrl}/invitation_notification`, {
        id: invitationNotification.id,
        state: 'rejected'
      }).pipe(catchError(this.errorHandleService.handleError))
        .subscribe((data: any) => {
          invitationNotification.state = 'rejected'
        })
    }
  }
}
