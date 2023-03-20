import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CircleMemberPageComponent } from './circle-member-page.component';

describe('CircleMemberPageComponent', () => {
  let component: CircleMemberPageComponent;
  let fixture: ComponentFixture<CircleMemberPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CircleMemberPageComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CircleMemberPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
