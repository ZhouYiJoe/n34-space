import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FilterSettingPageComponent } from './filter-setting-page.component';

describe('FilterSettingPageComponent', () => {
  let component: FilterSettingPageComponent;
  let fixture: ComponentFixture<FilterSettingPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FilterSettingPageComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FilterSettingPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
