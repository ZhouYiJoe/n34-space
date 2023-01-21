import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FolloweePostsPageComponent } from './followee-posts-page.component';

describe('FolloweePostsPageComponent', () => {
  let component: FolloweePostsPageComponent;
  let fixture: ComponentFixture<FolloweePostsPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FolloweePostsPageComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FolloweePostsPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
