import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {LoginPageComponent} from './components/login-page/login-page.component';
import {NotFoundPageComponent} from './components/not-found-page/not-found-page.component';
import {LoginFormComponent} from './components/login-page/login-form/login-form.component';
import {RegisterPageComponent} from './components/register-page/register-page.component';
import {RegisterFormComponent} from './components/register-page/register-form/register-form.component';
import {HttpClientModule} from "@angular/common/http";
import {FormsModule} from "@angular/forms";
import {HomePageComponent} from './components/main/home-page/home-page.component';
import {interceptorProviders} from "./interceptors";
import {MainComponent} from './components/main/main.component';
import {SidebarComponent} from './components/main/sidebar/sidebar.component';
import {PostPublishComponent} from './components/main/home-page/post-publish/post-publish.component';
import {PostsListComponent} from './components/main/posts-list/posts-list.component';
import {PostsListItemComponent} from './components/main/posts-list/posts-list-item/posts-list-item.component';
import {InfiniteScrollModule} from "ngx-infinite-scroll";
import { UserInfoPageComponent } from './components/main/user-info-page/user-info-page.component';
import { CommentListComponent } from './components/main/posts-list/posts-list-item/comment-list/comment-list.component';
import { HotPageComponent } from './components/main/hot-page/hot-page.component';
import { UserPageComponent } from './components/main/user-page/user-page.component';
import { UserPageHeaderComponent } from './components/main/user-page/user-page-header/user-page-header.component';
import { FolloweeListPageComponent } from './components/main/followee-list-page/followee-list-page.component';
import { FolloweeListItemComponent } from './components/main/followee-list-page/followee-list-item/followee-list-item.component';
import { FolloweePostsPageComponent } from './components/main/followee-posts-page/followee-posts-page.component';

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
    UserInfoPageComponent,
    CommentListComponent,
    HotPageComponent,
    UserPageComponent,
    UserPageHeaderComponent,
    FolloweeListPageComponent,
    FolloweeListItemComponent,
    FolloweePostsPageComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    InfiniteScrollModule
  ],
  providers: [interceptorProviders],
  bootstrap: [AppComponent]
})
export class AppModule { }

export const baseUrl: string = 'http://localhost:8080'
export const baseImgUrl: string = 'http://localhost:9090/images/'

export const currentUserIdKey = 'currentUserId'
export const currentUsernameKey = 'currentUsername'
export const currentUserEmailKey = 'currentUserEmail'
export const currentUserNicknameKey = 'currentUserNickname'
export const currentUserAvatarFilenameKey = 'currentUserAvatarFilename'