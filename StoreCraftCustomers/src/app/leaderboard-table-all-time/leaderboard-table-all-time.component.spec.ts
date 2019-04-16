import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LeaderboardTableAllTimeComponent } from './leaderboard-table-all-time.component';

describe('LeaderboardTableAllTimeComponent', () => {
  let component: LeaderboardTableAllTimeComponent;
  let fixture: ComponentFixture<LeaderboardTableAllTimeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LeaderboardTableAllTimeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LeaderboardTableAllTimeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
