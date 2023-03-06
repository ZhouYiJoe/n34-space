import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';

@Component({
  selector: 'app-reply-list',
  templateUrl: './reply-list.component.html',
  styleUrls: ['./reply-list.component.css']
})
export class ReplyListComponent implements OnInit {

  @Input()
  public replies: any[] = []

  @Output()
  public deleted: EventEmitter<any> = new EventEmitter<any>()

  @Output()
  public newReplyAddedEvent: EventEmitter<any> = new EventEmitter<any>()

  constructor() { }

  ngOnInit(): void {
  }

  deleteReply(replyId: any) {
    this.replies = this.replies.filter(reply => reply.id != replyId)
    this.deleted.emit()
  }

  newReplyAdded() {
    this.newReplyAddedEvent.emit()
  }

}
