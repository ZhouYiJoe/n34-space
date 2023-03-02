import { Component, OnInit } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {baseUrl} from "../../../app.module";
import {ActivatedRoute, NavigationExtras, Router} from "@angular/router";
import {catchError} from "rxjs";
import {ErrorHandleService} from "../../../services/error-handle.service";

@Component({
  selector: 'app-search-page',
  templateUrl: './search-page.component.html',
  styleUrls: ['./search-page.component.css']
})
export class SearchPageComponent implements OnInit {
  public posts: any[] = []
  public selected: boolean[] = [true, false, false]

  constructor(public httpClient: HttpClient,
              public activatedRoute: ActivatedRoute,
              public errorHandleService: ErrorHandleService,
              public router: Router) { }

  ngOnInit(): void {
  }

  onHotSelected() {
    this.selected = [true, false, false]
    this.activatedRoute.queryParams.subscribe((params: any) => {
      let navigationExtras: NavigationExtras = {queryParams: {q: params.q}}
      this.router.navigate(['/app/search/hot'], navigationExtras)
    })
  }

  onLatestSelected() {
    this.selected = [false, true, false]
    this.activatedRoute.queryParams.subscribe((params: any) => {
      let navigationExtras: NavigationExtras = {queryParams: {q: params.q}}
      this.router.navigate(['/app/search/latest'], navigationExtras)
    })
  }

  onUserSelected() {
    this.selected = [false, false, true]
    this.activatedRoute.queryParams.subscribe((params: any) => {
      let navigationExtras: NavigationExtras = {queryParams: {q: params.q}}
      this.router.navigate(['/app/search/user'], navigationExtras)
    })
  }

}
