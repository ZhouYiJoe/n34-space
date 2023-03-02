import { Component, OnInit } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {ActivatedRoute} from "@angular/router";
import {ErrorHandleService} from "../../../../services/error-handle.service";
import {baseUrl} from "../../../../app.module";
import {catchError} from "rxjs";

@Component({
  selector: 'app-search-latest-page',
  templateUrl: './search-latest-page.component.html',
  styleUrls: ['./search-latest-page.component.css']
})
export class SearchLatestPageComponent implements OnInit {

  public posts: any[] = []

  constructor(public httpClient: HttpClient,
              public activatedRoute: ActivatedRoute,
              public errorHandleService: ErrorHandleService) { }

  ngOnInit(): void {
    this.activatedRoute.queryParams.subscribe((params: any) => {
      this.httpClient.get(`${baseUrl}/posts/latest`,
        {params: {searchText: params.q}})
        .pipe(catchError(this.errorHandleService.handleError))
        .subscribe((data: any) => {
          this.posts = data
        })
    })
  }

}
