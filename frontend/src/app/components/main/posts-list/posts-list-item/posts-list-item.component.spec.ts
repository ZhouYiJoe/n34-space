import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PostsListItemComponent } from './posts-list-item.component';

describe('PostsListItemComponent', () => {
  let component: PostsListItemComponent;
  let fixture: ComponentFixture<PostsListItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PostsListItemComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PostsListItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
