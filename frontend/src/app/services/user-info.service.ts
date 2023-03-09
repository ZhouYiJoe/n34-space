import {Injectable} from '@angular/core';
import {baseUrl} from "../app.module";
import {Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class UserInfoService {
  public userInfo: any = null

  constructor(public router: Router,
              public httpClient: HttpClient) {
  }

  public getUserInfoRequest(): Observable<any> {
      return this.httpClient.get(`${baseUrl}/users/self`)
  }

}
