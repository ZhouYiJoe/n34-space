import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CircleListItemComponent } from './circle-list-item.component';

describe('CircleListItemComponent', () => {
  let component: CircleListItemComponent;
  let fixture: ComponentFixture<CircleListItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CircleListItemComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CircleListItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
