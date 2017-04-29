import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ResourcelistselectorComponent } from './resourcelistselector.component';

describe('ResourcelistselectorComponent', () => {
  let component: ResourcelistselectorComponent;
  let fixture: ComponentFixture<ResourcelistselectorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ResourcelistselectorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ResourcelistselectorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
