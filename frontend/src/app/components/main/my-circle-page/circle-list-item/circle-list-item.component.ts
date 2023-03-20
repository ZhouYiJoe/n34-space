import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {UserInfoService} from "../../../../services/user-info.service";
import {ErrorHandleService} from "../../../../services/error-handle.service";
import {catchError} from "rxjs";
import {baseUrl} from "../../../../app.module";

@Component({
  selector: 'app-circle-list-item',
  templateUrl: './circle-list-item.component.html',
  styleUrls: ['./circle-list-item.component.css']
})
export class CircleListItemComponent implements OnInit {

  @Input()
  public canRemove: boolean = false

  @Input()
  public circle: any = null

  public showMenu: boolean = false

  public showInviteeWindow: boolean = false

  public selectedIndex: number = 1

  public invitees: any[] = []

  @Input()
  public myId: string = ''

  @Output()
  public deleted: EventEmitter<any> = new EventEmitter<any>()

  constructor(public httpClient: HttpClient,
              public userInfoService: UserInfoService,
              public errorHandleService: ErrorHandleService) {
  }

  ngOnInit(): void {

  }

  delete() {
    if (confirm('确定要解散你的圈子吗？')) {
      this.httpClient.delete(`${baseUrl}/circle/${this.circle.id}`)
        .pipe(catchError(this.errorHandleService.handleError))
        .subscribe((data: any) => {
          this.deleted.emit(this.circle.id)
        })
    }
  }

  sendInvitation(invitee: any) {
    this.httpClient.post(`${baseUrl}/invitation_notification/sendInvitation`, {
      inviteeId: invitee.id,
      circleId: this.circle.id
    }).pipe(catchError(this.errorHandleService.handleError))
      .subscribe((data: any) => {
        alert('成功发送邀请')
      })
  }

  openInviteeWindow() {
    this.showInviteeWindow = true
    this.httpClient.get(`${baseUrl}/users/followee`, {
      params: {
        userId: this.myId
      }
    }).pipe(catchError(this.errorHandleService.handleError))
      .subscribe((data: any) => {
        this.invitees = data
      })
  }

  followeeSelected() {
    this.selectedIndex = 1
    this.httpClient.get(`${baseUrl}/users/followee`, {
      params: {
        userId: this.myId
      }
    }).pipe(catchError(this.errorHandleService.handleError))
      .subscribe((data: any) => {
        this.invitees = data
      })
  }

  followerSelected() {
    this.selectedIndex = 2
    this.httpClient.get(`${baseUrl}/users/follower`, {
      params: {
        userId: this.myId
      }
    }).pipe(catchError(this.errorHandleService.handleError))
      .subscribe((data: any) => {
        this.invitees = data
      })
  }

  quitCircle() {
    this.httpClient.delete(`${baseUrl}/circle/removeMembership`, {
      params: {
        circleId: this.circle.id,
        memberId: this.myId
      }
    }).pipe(catchError(this.errorHandleService.handleError))
      .subscribe((data: any) => {
        alert('退出成功')
        this.deleted.emit(this.circle.id)
      })
  }
}
