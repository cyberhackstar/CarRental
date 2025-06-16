import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TotalcarComponent } from './totalcar.component';

describe('TotalcarComponent', () => {
  let component: TotalcarComponent;
  let fixture: ComponentFixture<TotalcarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TotalcarComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TotalcarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
