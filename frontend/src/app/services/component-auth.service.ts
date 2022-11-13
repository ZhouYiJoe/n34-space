import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {catchError, Observable, throwError} from "rxjs";
import {baseUrl} from "../app.module";

@Injectable({
  providedIn: 'root'
})
export class ComponentAuthService implements CanActivate {

  constructor(public httpClient: HttpClient,
              public router: Router) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot):
    Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    let url: string = `${baseUrl}/auth/tokenValidation`
    this.httpClient.get<boolean>(url)
      .pipe(catchError((err => {
        this.router.navigate(['/login'])
        return throwError(() => new Error('认证失败'))
      })))
      .subscribe(data => {});
    return true
  }
}
