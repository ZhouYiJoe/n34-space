import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserInfoSettingPageComponent } from './user-info-setting-page.component';

describe('UserInfoSettingPageComponent', () => {
  let component: UserInfoSettingPageComponent;
  let fixture: ComponentFixture<UserInfoSettingPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UserInfoSettingPageComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserInfoSettingPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
