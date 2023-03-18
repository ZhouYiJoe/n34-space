import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {baseImgUrl, baseUrl} from "../../../app.module";
import {ActivatedRoute, Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {catchError} from "rxjs";
import {ErrorHandleService} from "../../../services/error-handle.service";
import {UserInfoService} from "../../../services/user-info.service";

@Component({
  selector: 'app-message-page',
  templateUrl: './message-page.component.html',
  styleUrls: ['./message-page.component.css']
})
export class MessagePageComponent implements OnInit {

  @ViewChild('messagesDiv')
  public messagesDiv: any

  public messageUserList: any[] = []

  public user: any = null

  public messages: any[] = []

  public selectedIndex: number = -1

  public messageInput: string = ''

  public myId: string = ''

  constructor(public router: Router,
              public httpClient: HttpClient,
              public errorHandleService: ErrorHandleService,
              public activateRoute: ActivatedRoute,
              public userInfoService: UserInfoService) {
  }

  ngOnInit(): void {
    this.userInfoService.getUserInfoRequest()
      .pipe(catchError(this.errorHandleService.handleError))
      .subscribe((data: any) => {
        this.myId = data.id
      })

    this.activateRoute.queryParams.subscribe((params: any) => {
        if (params.to) {
          this.httpClient.get(`${baseUrl}/users/${params.to}`)
            .pipe(catchError(this.errorHandleService.handleError))
            .subscribe((data: any) => {
              this.user = data
              this.httpClient.get(`${baseUrl}/message/getMessagesBetweenMeAndOther`,
                {params: {otherUserId: this.user.id}})
                .pipe(catchError(this.errorHandleService.handleError))
                .subscribe((data: any) => {
                  this.messages = data
                  this.moveMessageWindowToBottom()
                })
            })
        }
      }
    )

    this.httpClient.get(`${baseUrl}/message/getMessageUserList`)
      .pipe(catchError(this.errorHandleService.handleError))
      .subscribe((data: any) => {
        console.log(data);
        this.messageUserList = data
      })
  }

  moveMessageWindowToBottom() {
    setTimeout(() => {
      const {clientHeight, scrollHeight} = this.messagesDiv.nativeElement
      this.messagesDiv.nativeElement.scrollTop = scrollHeight - clientHeight
    }, 10)
  }

  select(index: number) {
    this.selectedIndex = index
    this.httpClient.get(`${baseUrl}/users/${this.messageUserList[index].username}`)
      .pipe(catchError(this.errorHandleService.handleError))
      .subscribe((data: any) => {
        this.user = data
        this.httpClient.get(`${baseUrl}/message/getMessagesBetweenMeAndOther`,
          {params: {otherUserId: this.user.id}})
          .pipe(catchError(this.errorHandleService.handleError))
          .subscribe((data: any) => {
            this.messages = data
            this.moveMessageWindowToBottom()
          })
      })
  }

  sendMessage() {
    this.httpClient.post(`${baseUrl}/message`, {
      senderId: this.myId,
      receiverId: this.user.id,
      content: this.messageInput
    }).pipe(catchError(this.errorHandleService.handleError))
      .subscribe((data: any) => {
        if (data) {
          this.messages.push(data)
          this.moveMessageWindowToBottom()
          this.messageInput = ''
        } else {
          alert('发送失败')
        }
      })
  }

}
