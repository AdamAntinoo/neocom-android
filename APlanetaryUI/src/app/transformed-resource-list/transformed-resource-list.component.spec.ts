import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TransformedResourceListComponent } from './transformed-resource-list.component';

describe('TransformedResourceListComponent', () => {
  let component: TransformedResourceListComponent;
  let fixture: ComponentFixture<TransformedResourceListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TransformedResourceListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TransformedResourceListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
