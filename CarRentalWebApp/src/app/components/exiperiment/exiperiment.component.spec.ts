import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExiperimentComponent } from './exiperiment.component';

describe('ExiperimentComponent', () => {
  let component: ExiperimentComponent;
  let fixture: ComponentFixture<ExiperimentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ExiperimentComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ExiperimentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
