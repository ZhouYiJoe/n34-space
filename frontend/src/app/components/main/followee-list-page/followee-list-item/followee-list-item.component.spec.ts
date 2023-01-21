import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FolloweeListItemComponent } from './followee-list-item.component';

describe('FolloweeListItemComponent', () => {
  let component: FolloweeListItemComponent;
  let fixture: ComponentFixture<FolloweeListItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FolloweeListItemComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FolloweeListItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
