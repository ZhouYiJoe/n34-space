import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FollowerListPageComponent } from './follower-list-page.component';

describe('FollowerListPageComponent', () => {
  let component: FollowerListPageComponent;
  let fixture: ComponentFixture<FollowerListPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FollowerListPageComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FollowerListPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
