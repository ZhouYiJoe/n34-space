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
    if (this.userInfo == null) {
      let observable: Observable<any> = this.httpClient.get(`${baseUrl}/users/self`)
      observable.subscribe((data: any) => {
        this.userInfo = data
      })
      return observable
    } else {
      return new Observable<any>(observer => {
        setTimeout(() => {
          observer.next(this.userInfo)
        }, 0)
      })
    }
  }

}
