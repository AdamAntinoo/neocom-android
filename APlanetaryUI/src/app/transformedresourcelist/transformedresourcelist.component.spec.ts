import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TransformedresourcelistComponent } from './transformedresourcelist.component';

describe('TransformedresourcelistComponent', () => {
  let component: TransformedresourcelistComponent;
  let fixture: ComponentFixture<TransformedresourcelistComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TransformedresourcelistComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TransformedresourcelistComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
