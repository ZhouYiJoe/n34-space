import { Component, OnInit } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {baseUrl} from "../../../app.module";
import {ActivatedRoute} from "@angular/router";
import {catchError} from "rxjs";
import {ErrorHandleService} from "../../../services/error-handle.service";

@Component({
  selector: 'app-search-page',
  templateUrl: './search-page.component.html',
  styleUrls: ['./search-page.component.css']
})
export class SearchPageComponent implements OnInit {
  public posts: any[] = []

  constructor(public httpClient: HttpClient,
              public activatedRoute: ActivatedRoute,
              public errorHandleService: ErrorHandleService) { }

  ngOnInit(): void {
    this.activatedRoute.queryParams.subscribe((params: any) => {
      this.httpClient.get(`${baseUrl}/posts/hot`,
        {params: {searchText: params.q}})
        .pipe(catchError(this.errorHandleService.handleError))
        .subscribe((data: any) => {
          this.posts = data
      })
    })
  }

}
