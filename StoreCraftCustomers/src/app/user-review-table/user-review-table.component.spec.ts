import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UserReviewTableComponent } from './user-review-table.component';

describe('UserReviewTableComponent', () => {
  let component: UserReviewTableComponent;
  let fixture: ComponentFixture<UserReviewTableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UserReviewTableComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UserReviewTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
