import {Injectable} from "@angular/core";
import {HttpClient, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {Observable} from "rxjs";
import {baseUrl} from "../app.module";

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(public httpClient: HttpClient) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (req.url === `${baseUrl}/auth/login` || req.url === `${baseUrl}/auth/register`)
      return next.handle(req)
    let token: string | null = localStorage.getItem('token')
    if (token === null) token = ''
    let authReq: HttpRequest<any> = req.clone({setHeaders: {'token': token}});
    return next.handle(authReq)
  }
}
