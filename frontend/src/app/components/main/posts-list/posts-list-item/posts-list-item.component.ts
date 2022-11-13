import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-posts-list-item',
  templateUrl: './posts-list-item.component.html',
  styleUrls: ['./posts-list-item.component.css']
})
export class PostsListItemComponent implements OnInit {
  @Input()
  public post: any

  constructor() { }

  ngOnInit(): void {
  }

}
