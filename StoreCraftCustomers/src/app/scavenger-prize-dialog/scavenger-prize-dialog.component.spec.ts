import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ScavengerPrizeDialogComponent } from './scavenger-prize-dialog.component';

describe('ScavengerPrizeDialogComponent', () => {
  let component: ScavengerPrizeDialogComponent;
  let fixture: ComponentFixture<ScavengerPrizeDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ScavengerPrizeDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ScavengerPrizeDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
