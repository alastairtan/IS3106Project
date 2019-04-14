import { TestBed } from '@angular/core/testing';

import { CommunityGoalService } from './community-goal.service';

describe('CommunityGoalService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: CommunityGoalService = TestBed.get(CommunityGoalService);
    expect(service).toBeTruthy();
  });
});
