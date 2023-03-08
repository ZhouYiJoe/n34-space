import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";

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
    this.router.navigate(['/app/home'])
  }
}
