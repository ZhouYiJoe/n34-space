import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';

import {LoginPageComponent} from './components/login-page/login-page.component';
import {NotFoundPageComponent} from "./components/not-found-page/not-found-page.component";
import {RegisterPageComponent} from "./components/register-page/register-page.component";
import {HomePageComponent} from "./components/main/home-page/home-page.component";
import {ComponentAuthService} from "./services/component-auth.service";
import {MainComponent} from "./components/main/main.component";
import {UserInfoPageComponent} from "./components/main/user-info-page/user-info-page.component";
import {HotPageComponent} from "./components/main/hot-page/hot-page.component";
import {UserPageComponent} from "./components/main/user-page/user-page.component";
import {FolloweeListPageComponent} from "./components/main/followee-list-page/followee-list-page.component";
import {FolloweePostsPageComponent} from "./components/main/followee-posts-page/followee-posts-page.component";

const routes: Routes = [
  {
    path: 'login',
    component: LoginPageComponent
  },
  {
    path: 'register',
    component: RegisterPageComponent
  },
  {
    path: '404',
    component: NotFoundPageComponent
  },
  {
    path: 'app',
    component: MainComponent,
    canActivate: [ComponentAuthService],
    children: [
      {
        path: 'home',
        component: HomePageComponent
      },
      {
        path: 'hot',
        component: HotPageComponent
      },
      {
        path: 'user_info',
        component: UserInfoPageComponent
      },
      {
        path: 'users/:username',
        component: UserPageComponent
      },
      {
        path: 'followee_list',
        component: FolloweeListPageComponent
      },
      {
        path: 'followee_posts',
        component: FolloweePostsPageComponent
      },
      {
        path: '',
        pathMatch: 'full',
        redirectTo: 'home'
      }
    ]
  },
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'app'
  },
  {
    path: '**',
    redirectTo: '404'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}