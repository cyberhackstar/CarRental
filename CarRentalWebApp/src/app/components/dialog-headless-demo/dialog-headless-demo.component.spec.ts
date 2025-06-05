import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogHeadlessDemoComponent } from './dialog-headless-demo.component';

describe('DialogHeadlessDemoComponent', () => {
  let component: DialogHeadlessDemoComponent;
  let fixture: ComponentFixture<DialogHeadlessDemoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DialogHeadlessDemoComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DialogHeadlessDemoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
