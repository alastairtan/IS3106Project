import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ScavengerHuntWinnersIndexComponent } from './scavenger-hunt-winners-index.component';

describe('ScavengerHuntWinnersIndexComponent', () => {
  let component: ScavengerHuntWinnersIndexComponent;
  let fixture: ComponentFixture<ScavengerHuntWinnersIndexComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ScavengerHuntWinnersIndexComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ScavengerHuntWinnersIndexComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
