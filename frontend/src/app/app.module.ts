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
import { HomePageComponent } from './components/main/home-page/home-page.component';
import {interceptorProviders} from "./interceptors";
import { MainComponent } from './components/main/main.component';
import { SidebarComponent } from './components/main/sidebar/sidebar.component';
import { PostPublishComponent } from './components/main/home-page/post-publish/post-publish.component';
import { PostsListComponent } from './components/main/posts-list/posts-list.component';
import { PostsListItemComponent } from './components/main/posts-list/posts-list-item/posts-list-item.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginPageComponent,
    NotFoundPageComponent,
    LoginFormComponent,
    RegisterPageComponent,
    RegisterFormComponent,
    HomePageComponent,
    MainComponent,
    SidebarComponent,
    PostPublishComponent,
    PostsListComponent,
    PostsListItemComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [interceptorProviders],
  bootstrap: [AppComponent]
})
export class AppModule { }

export const baseUrl: string = 'http://localhost:8080'
