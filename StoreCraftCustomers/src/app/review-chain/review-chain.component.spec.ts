import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewChainComponent } from './review-chain.component';

describe('ReviewChainComponent', () => {
  let component: ReviewChainComponent;
  let fixture: ComponentFixture<ReviewChainComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ReviewChainComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReviewChainComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
