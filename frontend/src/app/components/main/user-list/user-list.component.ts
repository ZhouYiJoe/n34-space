import {Component, Input, OnInit} from '@angular/core';
import {UserInfoService} from "../../../services/user-info.service";
import {catchError} from "rxjs";
import {ErrorHandleService} from "../../../services/error-handle.service";

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {
  @Input()
  public users: any[] = []

  public myId: string = ''

  constructor(public userInfoService: UserInfoService,
              public errorHandleService: ErrorHandleService) { }

  ngOnInit(): void {
    this.userInfoService.getUserInfoRequest()
      .pipe(catchError(this.errorHandleService.handleError))
      .subscribe((user: any) => {
        this.myId = user.id
      })
  }

}
