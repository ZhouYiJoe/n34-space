import {Component, OnInit, ViewChild} from '@angular/core';
import {NavigationExtras, Router} from "@angular/router";

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent implements OnInit {
  @ViewChild('content')
  public content: any

  public searchText: string = ''

  constructor(public router: Router) {
  }

  ngOnInit(): void {

  }

  search(event: KeyboardEvent) {
    if (event.key == 'Enter') {
      let navigationExtras: NavigationExtras = {queryParams: {q: this.searchText}}
      this.router.navigate(['/app/search'], navigationExtras)
    }
  }
}
