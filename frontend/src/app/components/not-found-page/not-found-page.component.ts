import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";
import {currentUsernameKey} from "../../app.module";

@Component({
  selector: 'app-not-found-page',
  templateUrl: './not-found-page.component.html',
  styleUrls: ['./not-found-page.component.css']
})
export class NotFoundPageComponent implements OnInit {

  constructor(public router: Router) { }

  ngOnInit(): void {
  }

  backToMainPage() {
    let curUsername = localStorage.getItem(currentUsernameKey)
    if (curUsername === null) {
      this.router.navigate(['/login'])
    }
    this.router.navigate(['/app/home'])
  }
}
