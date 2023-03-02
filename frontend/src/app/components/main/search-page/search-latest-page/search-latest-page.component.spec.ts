import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchLatestPageComponent } from './search-latest-page.component';

describe('SearchLatestPageComponent', () => {
  let component: SearchLatestPageComponent;
  let fixture: ComponentFixture<SearchLatestPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SearchLatestPageComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SearchLatestPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
