import { Component, OnInit } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {
  baseUrl, currentFilterConfigKey,
  currentUserAvatarFilenameKey,
  currentUserEmailKey,
  currentUserIdKey, currentUsernameKey,
  currentUserNicknameKey
} from "../../../app.module";
import {catchError} from "rxjs";
import {ErrorHandleService} from "../../../services/error-handle.service";

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent implements OnInit {

  constructor(public httpClient: HttpClient,
              public router: Router,
              public errorHandleService: ErrorHandleService) { }

  ngOnInit(): void {
  }

  logout() {
    this.httpClient.get(`${baseUrl}/auth/logout`)
      .pipe(catchError(this.errorHandleService.handleError))
      .subscribe((data: any) => {})
    localStorage.removeItem('token')
    localStorage.removeItem(currentUserIdKey)
    localStorage.removeItem(currentUserEmailKey)
    localStorage.removeItem(currentUserNicknameKey)
    localStorage.removeItem(currentUserAvatarFilenameKey)
    localStorage.removeItem(currentUsernameKey)
    localStorage.removeItem(currentFilterConfigKey)
    this.router.navigate(['login'])
  }
}
