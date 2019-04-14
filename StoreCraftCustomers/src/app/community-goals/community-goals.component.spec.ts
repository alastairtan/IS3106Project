
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CommunityGoalsComponent } from './community-goals.component';

describe('CommunityGoalsComponent', () => {
  let component: CommunityGoalsComponent;
  let fixture: ComponentFixture<CommunityGoalsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CommunityGoalsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CommunityGoalsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

