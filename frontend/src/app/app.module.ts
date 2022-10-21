import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginPageComponent } from './components/login-page/login-page.component';
import { NotFoundPageComponent } from './components/not-found-page/not-found-page.component';
import { LoginFormComponent } from './components/login-page/login-form/login-form.component';
import { RegisterPageComponent } from './components/register-page/register-page.component';
import { RegisterFormComponent } from './components/register-page/register-form/register-form.component';
import {HttpClientModule, HttpErrorResponse} from "@angular/common/http";
import {FormsModule} from "@angular/forms";
import {Observable, throwError} from "rxjs";
import { HomePageComponent } from './components/home-page/home-page.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginPageComponent,
    NotFoundPageComponent,
    LoginFormComponent,
    RegisterPageComponent,
    RegisterFormComponent,
    HomePageComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }

export const baseApi: string = 'http://localhost:8080'

export function handleError(response: HttpErrorResponse): Observable<never> {
  console.error(response)
  let responseBody = typeof response.error === 'string' ? JSON.parse(response.error) : response.error
  alert(responseBody.message)
  return throwError(() => new Error(response.message))
}
