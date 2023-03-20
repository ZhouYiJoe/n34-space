import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MyCirclePageComponent } from './my-circle-page.component';

describe('MyCirclePageComponent', () => {
  let component: MyCirclePageComponent;
  let fixture: ComponentFixture<MyCirclePageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MyCirclePageComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MyCirclePageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
