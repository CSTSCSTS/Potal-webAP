import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PotalComponent } from './potal.component';

describe('PotalComponent', () => {
  let component: PotalComponent;
  let fixture: ComponentFixture<PotalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PotalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PotalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
