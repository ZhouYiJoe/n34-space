import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchHotPageComponent } from './search-hot-page.component';

describe('SearchHotPageComponent', () => {
  let component: SearchHotPageComponent;
  let fixture: ComponentFixture<SearchHotPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SearchHotPageComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SearchHotPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
