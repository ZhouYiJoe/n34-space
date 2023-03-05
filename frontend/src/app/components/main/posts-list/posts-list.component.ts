import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-posts-list',
  templateUrl: './posts-list.component.html',
  styleUrls: ['./posts-list.component.css']
})
export class PostsListComponent implements OnInit {
  @Input()
  public posts: any[] = []

  @Input()
  public canEdit: boolean = false

  constructor() { }

  ngOnInit(): void {
  }

  deletePost(postId: any) {
    this.posts = this.posts.filter(post => post.id != postId)
  }

}
