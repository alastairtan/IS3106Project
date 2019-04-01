import { TestBed } from '@angular/core/testing';

import { ScavengerHuntService } from './scavenger-hunt.service';

describe('ScavengerHuntService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ScavengerHuntService = TestBed.get(ScavengerHuntService);
    expect(service).toBeTruthy();
  });
});
