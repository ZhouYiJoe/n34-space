import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {baseUrl} from "../../../app.module";
import {catchError} from "rxjs";
import {ErrorHandleService} from "../../../services/error-handle.service";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-followee-list-page',
  templateUrl: './followee-list-page.component.html',
  styleUrls: ['./followee-list-page.component.css']
})
export class FolloweeListPageComponent implements OnInit {
  public followees: any[] = []

  constructor(public httpClient: HttpClient,
              public errorHandleService: ErrorHandleService,
              public activatedRoute: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe((params: any) => {
      this.httpClient.get(`${baseUrl}/users/followee`,
        {params: {userId: params.followerId}})
        .pipe(catchError(this.errorHandleService.handleError))
        .subscribe((data: any) => {
          this.followees = data
        })
    })

  }

}
