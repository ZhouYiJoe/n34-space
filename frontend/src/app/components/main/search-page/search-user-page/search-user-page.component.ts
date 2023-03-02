import { Component, OnInit } from '@angular/core';
import {baseUrl} from "../../../../app.module";
import {catchError} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {ActivatedRoute} from "@angular/router";
import {ErrorHandleService} from "../../../../services/error-handle.service";

@Component({
  selector: 'app-search-user-page',
  templateUrl: './search-user-page.component.html',
  styleUrls: ['./search-user-page.component.css']
})
export class SearchUserPageComponent implements OnInit {
  public users: any[] = []

  constructor(public httpClient: HttpClient,
              public activatedRoute: ActivatedRoute,
              public errorHandleService: ErrorHandleService) { }

  ngOnInit(): void {
    this.activatedRoute.queryParams.subscribe((params: any) => {
      this.httpClient.get(`${baseUrl}/users`,
        {params: {searchText: params.q}})
        .pipe(catchError(this.errorHandleService.handleError))
        .subscribe((data: any) => {
          this.users = data
        })
    })
  }

}
