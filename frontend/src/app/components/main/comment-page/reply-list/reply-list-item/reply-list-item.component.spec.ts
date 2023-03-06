import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReplyListItemComponent } from './reply-list-item.component';

describe('ReplyListItemComponent', () => {
  let component: ReplyListItemComponent;
  let fixture: ComponentFixture<ReplyListItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReplyListItemComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReplyListItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
