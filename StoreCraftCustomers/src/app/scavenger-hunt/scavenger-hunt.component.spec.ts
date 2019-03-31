import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ScavengerHuntComponent } from './scavenger-hunt.component';

describe('ScavengerHuntComponent', () => {
  let component: ScavengerHuntComponent;
  let fixture: ComponentFixture<ScavengerHuntComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ScavengerHuntComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ScavengerHuntComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
