import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FolloweeListPageComponent } from './followee-list-page.component';

describe('FolloweeListPageComponent', () => {
  let component: FolloweeListPageComponent;
  let fixture: ComponentFixture<FolloweeListPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FolloweeListPageComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FolloweeListPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
