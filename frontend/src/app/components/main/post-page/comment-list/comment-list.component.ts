import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';

@Component({
  selector: 'app-comment-list',
  templateUrl: './comment-list.component.html',
  styleUrls: ['./comment-list.component.css']
})
export class CommentListComponent implements OnInit {

  @Input()
  public comments: any[] = []

  @Output()
  public deleted: EventEmitter<any> = new EventEmitter<any>()

  constructor() { }

  ngOnInit(): void {
  }

  deleteComment(commentId: any) {
    this.comments = this.comments.filter(comment => comment.id != commentId)
    this.deleted.emit()
  }

}
