import { Component, OnInit } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {ErrorHandleService} from "../../../services/error-handle.service";
import {ActivatedRoute} from "@angular/router";
import {baseUrl} from "../../../app.module";
import {catchError} from "rxjs";

@Component({
  selector: 'app-follower-list-page',
  templateUrl: './follower-list-page.component.html',
  styleUrls: ['./follower-list-page.component.css']
})
export class FollowerListPageComponent implements OnInit {

  public followers: any[] = []

  constructor(public httpClient: HttpClient,
              public errorHandleService: ErrorHandleService,
              public activatedRoute: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe((params: any) => {
      this.httpClient.get(`${baseUrl}/users/follower`,
        {params: {userId: params.followeeId}})
        .pipe(catchError(this.errorHandleService.handleError))
        .subscribe((data: any) => {
          this.followers = data
        })
    })

  }

}
