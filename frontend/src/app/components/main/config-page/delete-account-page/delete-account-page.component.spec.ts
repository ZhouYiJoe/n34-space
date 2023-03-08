import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeleteAccountPageComponent } from './delete-account-page.component';

describe('DeleteAccountPageComponent', () => {
  let component: DeleteAccountPageComponent;
  let fixture: ComponentFixture<DeleteAccountPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DeleteAccountPageComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DeleteAccountPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
