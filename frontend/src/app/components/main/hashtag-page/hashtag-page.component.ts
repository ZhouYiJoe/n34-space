import {Component, OnInit, ViewChild} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {ActivatedRoute, Router} from "@angular/router";
import {ErrorHandleService} from "../../../services/error-handle.service";
import {baseUrl} from "../../../app.module";
import {catchError} from "rxjs";

@Component({
  selector: 'app-hashtag-page',
  templateUrl: './hashtag-page.component.html',
  styleUrls: ['./hashtag-page.component.css']
})
export class HashtagPageComponent implements OnInit {
  public posts: any[] = []
  public selected: boolean[] = [false, false]

  constructor(public httpClient: HttpClient,
              public activatedRoute: ActivatedRoute,
              public errorHandleService: ErrorHandleService,
              public router: Router) {
  }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe((params: any) => {
      this.httpClient.get(`${baseUrl}/posts/hot`,
        {params: {hashtag: params.hashtag}})
        .pipe(catchError(this.errorHandleService.handleError))
        .subscribe((data: any) => {
          this.selected = [true, false]
          this.posts = data
        })
    })
  }

  onHotSelected() {
    this.selected = [true, false]
    this.activatedRoute.params.subscribe((params: any) => {
      this.httpClient.get(`${baseUrl}/posts/hot`,
        {params: {hashtag: params.hashtag}})
        .pipe(catchError(this.errorHandleService.handleError))
        .subscribe((data: any) => {
          this.posts = data
        })
    })
  }

  onLatestSelected() {
    this.selected = [false, true]
    this.activatedRoute.params.subscribe((params: any) => {
      this.httpClient.get(`${baseUrl}/posts/latest`,
        {params: {hashtag: params.hashtag}})
        .pipe(catchError(this.errorHandleService.handleError))
        .subscribe((data: any) => {
          this.posts = data
          console.log(this.posts)
        })
    })
  }

}
