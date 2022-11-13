import { Injectable } from '@angular/core';
import {HttpErrorResponse} from "@angular/common/http";
import {Observable, throwError} from "rxjs";
import {Router} from "@angular/router";

@Injectable({
  providedIn: 'root'
})
export class ErrorHandleService {
  constructor(public router: Router) { }

  handleError(response: HttpErrorResponse): Observable<never> {
    console.error(response)
    let responseBody = typeof response.error === 'string' ? JSON.parse(response.error) : response.error
    if (responseBody.status === 403) {
      this.router.navigate(['/login'])
    } else {
      alert(responseBody.message)
    }
    return throwError(() => new Error(response.message))
  }
}
