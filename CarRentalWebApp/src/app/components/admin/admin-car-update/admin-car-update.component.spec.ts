import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminCarUpdateComponent } from './admin-car-update.component';

describe('AdminCarUpdateComponent', () => {
  let component: AdminCarUpdateComponent;
  let fixture: ComponentFixture<AdminCarUpdateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminCarUpdateComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminCarUpdateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
