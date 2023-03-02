import { Component, OnInit } from '@angular/core';
import {baseUrl} from "../../../../app.module";
import {catchError} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {ActivatedRoute} from "@angular/router";
import {ErrorHandleService} from "../../../../services/error-handle.service";

@Component({
  selector: 'app-search-hot-page',
  templateUrl: './search-hot-page.component.html',
  styleUrls: ['./search-hot-page.component.css']
})
export class SearchHotPageComponent implements OnInit {
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
