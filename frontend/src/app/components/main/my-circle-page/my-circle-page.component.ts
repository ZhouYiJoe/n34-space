import { Component, OnInit } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {UserInfoService} from "../../../services/user-info.service";
import {ErrorHandleService} from "../../../services/error-handle.service";
import {catchError} from "rxjs";
import {baseUrl} from "../../../app.module";

@Component({
  selector: 'app-my-circle-page',
  templateUrl: './my-circle-page.component.html',
  styleUrls: ['./my-circle-page.component.css']
})
export class MyCirclePageComponent implements OnInit {

  public circles: any[] = []

  public myId: string = ''

  public showCreateCircleWindow: boolean = false

  public nameInput: string = ''

  public introductionInput: string = ''

  constructor(public httpClient: HttpClient,
              public userInfoService: UserInfoService,
              public errorHandleService: ErrorHandleService) { }

  ngOnInit(): void {
    this.userInfoService.getUserInfoRequest()
      .pipe(catchError(this.errorHandleService.handleError))
      .subscribe((user: any) => {
        this.myId = user.id
        this.httpClient.get(`${baseUrl}/circle/circlesIJoined`)
          .pipe(catchError(this.errorHandleService.handleError))
          .subscribe((data: any) => {
            this.circles = data
          })
      })
  }

  createCircle() {
    this.httpClient.post(`${baseUrl}/circle`, {
      creatorId: this.myId,
      name: this.nameInput,
      introduction: this.introductionInput
    }).pipe(catchError(this.errorHandleService.handleError))
      .subscribe((data: any) => {
        if (data) {
          alert('创建成功')
          this.showCreateCircleWindow = false
          this.circles.unshift(data)
        } else {
          alert('创建失败')
        }
      })
  }

  deleteCircle(circleId: string) {
    this.circles = this.circles.filter(circle => circle.id != circleId)
  }
}
