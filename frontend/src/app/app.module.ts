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
import { HotPageComponent } from './components/main/hot-page/hot-page.component';
import { UserPageComponent } from './components/main/user-page/user-page.component';
import { FolloweeListPageComponent } from './components/main/followee-list-page/followee-list-page.component';
import { FolloweeListItemComponent } from './components/main/followee-list-page/followee-list-item/followee-list-item.component';
import { FolloweePostsPageComponent } from './components/main/followee-posts-page/followee-posts-page.component';
import { ConfigPageComponent } from './components/main/config-page/config-page.component';
import { SearchPageComponent } from './components/main/search-page/search-page.component';
import { SearchHotPageComponent } from './components/main/search-page/search-hot-page/search-hot-page.component';
import { SearchLatestPageComponent } from './components/main/search-page/search-latest-page/search-latest-page.component';
import { SearchUserPageComponent } from './components/main/search-page/search-user-page/search-user-page.component';
import { UserListComponent } from './components/main/user-list/user-list.component';
import { UserListItemComponent } from './components/main/user-list/user-list-item/user-list-item.component';
import { HashtagPageComponent } from './components/main/hashtag-page/hashtag-page.component';
import {Safe} from "./safe";
import { PostPageComponent } from './components/main/post-page/post-page.component';
import { CommentListItemComponent } from './components/main/post-page/comment-list/comment-list-item/comment-list-item.component';
import {CommentListComponent} from "./components/main/post-page/comment-list/comment-list.component";
import { CommentPageComponent } from './components/main/comment-page/comment-page.component';
import { ReplyListComponent } from './components/main/comment-page/reply-list/reply-list.component';
import { ReplyListItemComponent } from './components/main/comment-page/reply-list/reply-list-item/reply-list-item.component';
import { FollowerListPageComponent } from './components/main/follower-list-page/follower-list-page.component';
import { FilterSettingPageComponent } from './components/main/config-page/filter-setting-page/filter-setting-page.component';
import { UserInfoSettingPageComponent } from './components/main/config-page/user-info-setting-page/user-info-setting-page.component';
import { ChangePasswordPageComponent } from './components/main/config-page/change-password-page/change-password-page.component';
import { DeleteAccountPageComponent } from './components/main/config-page/delete-account-page/delete-account-page.component';
import { NotificationPageComponent } from './components/main/notification-page/notification-page.component';
import { MessagePageComponent } from './components/main/message-page/message-page.component';
import { CirclePageComponent } from './components/main/circle-page/circle-page.component';
import { MyCirclePageComponent } from './components/main/my-circle-page/my-circle-page.component';
import { CircleListItemComponent } from './components/main/my-circle-page/circle-list-item/circle-list-item.component';
import { CircleMemberPageComponent } from './components/main/circle-member-page/circle-member-page.component';

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
    CommentListItemComponent,
    HotPageComponent,
    UserPageComponent,
    FolloweeListPageComponent,
    FolloweeListItemComponent,
    FolloweePostsPageComponent,
    ConfigPageComponent,
    SearchPageComponent,
    SearchHotPageComponent,
    SearchLatestPageComponent,
    SearchUserPageComponent,
    UserListComponent,
    UserListItemComponent,
    HashtagPageComponent,
    Safe,
    PostPageComponent,
    CommentListItemComponent,
    CommentPageComponent,
    ReplyListComponent,
    ReplyListItemComponent,
    FollowerListPageComponent,
    FilterSettingPageComponent,
    UserInfoSettingPageComponent,
    ChangePasswordPageComponent,
    DeleteAccountPageComponent,
    NotificationPageComponent,
    MessagePageComponent,
    CirclePageComponent,
    MyCirclePageComponent,
    CircleListItemComponent,
    CircleMemberPageComponent,
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
