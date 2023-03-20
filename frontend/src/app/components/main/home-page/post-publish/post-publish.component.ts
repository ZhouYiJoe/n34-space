import {Component, Input, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {baseUrl} from "../../../../app.module";
import {catchError} from "rxjs";
import {ErrorHandleService} from "../../../../services/error-handle.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-post-publish',
  templateUrl: './post-publish.component.html',
  styleUrls: ['./post-publish.component.css']
})
export class PostPublishComponent implements OnInit {
  @Input()
  public postsComponent: any

  public postToPublish: any = {content: ''}

  constructor(public httpClient: HttpClient,
              public errorHandleService: ErrorHandleService,
              public router: Router) {
  }

  ngOnInit(): void {

  }

  publishPost(): void {
    this.httpClient.post(`${baseUrl}/posts`, this.postToPublish)
      .pipe(catchError(this.errorHandleService.handleError))
      .subscribe(data => {
        if (!data) {
          alert('推文发布失败，原因未知')
        } else {
          alert('发布成功！')
          this.postToPublish.content = ''
          location.reload()
        }
      })
  }

}
