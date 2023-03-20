import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {ErrorHandleService} from "../../../services/error-handle.service";
import {ActivatedRoute, Router} from "@angular/router";
import {UserInfoService} from "../../../services/user-info.service";
import {baseImgUrl, baseUrl} from "../../../app.module";
import {catchError} from "rxjs";

@Component({
  selector: 'app-circle-member-page',
  templateUrl: './circle-member-page.component.html',
  styleUrls: ['./circle-member-page.component.css']
})
export class CircleMemberPageComponent implements OnInit {

  public members: any[] = []

  public circle: any = null

  constructor(public httpClient: HttpClient,
              public errorHandleService: ErrorHandleService,
              public router: Router,
              public userInfoService: UserInfoService,
              public activatedRoute: ActivatedRoute) {
  }

  ngOnInit(): void {

    this.activatedRoute.params.subscribe((params: any) => {
      this.httpClient.get(`${baseUrl}/users/getCircleMembers`, {
        params: {
          circleId: params.circleId
        }
      }).pipe(catchError(this.errorHandleService.handleError))
        .subscribe((data: any) => {
          this.members = data
        })
      this.httpClient.get(`${baseUrl}/circle/${params.circleId}`)
        .pipe(catchError(this.errorHandleService.handleError))
        .subscribe((data: any) => {
          this.circle = data
        })
    })
  }

}
