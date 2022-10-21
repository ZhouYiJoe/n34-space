import { TestBed } from '@angular/core/testing';

import { ComponentAuthService } from './component-auth.service';

describe('ComponentAuthService', () => {
  let service: ComponentAuthService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ComponentAuthService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
