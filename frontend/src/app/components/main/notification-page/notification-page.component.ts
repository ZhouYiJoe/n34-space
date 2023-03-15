import { Component, OnInit } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {baseUrl} from "../../../app.module";
import {catchError} from "rxjs";
import {ErrorHandleService} from "../../../services/error-handle.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-notification-page',
  templateUrl: './notification-page.component.html',
  styleUrls: ['./notification-page.component.css']
})
export class NotificationPageComponent implements OnInit {
  public selectedIndex: number = 0
  public mentionNotifications: any[] = []

  constructor(public httpClient: HttpClient,
              public errorHandleService: ErrorHandleService,
              public router: Router) { }

  ngOnInit(): void {
    this.commentNaviSelected()
  }

  commentNaviSelected() {
    this.selectedIndex = 1
  }

  mentionNaviSelected() {
    this.selectedIndex = 2
    this.httpClient.get(`${baseUrl}/mention_notification/getNewNotification`)
      .pipe(catchError(this.errorHandleService.handleError))
      .subscribe((data: any) => {
        this.mentionNotifications = data
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

  onClickInMentionNotification(event: any, mentionNotification: any) {
    if (!event.target.attributes.link || !event.target.attributes.param) {
      this.jumpToMention(mentionNotification)
      return
    }
    let link = event.target.attributes.link.value
    let param = event.target.attributes.param.value
    this.router.navigate([link, param])
  }

}
