import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PlanetaryresourcesHomeComponent } from './planetaryresources-home.component';

describe('PlanetaryresourcesHomeComponent', () => {
  let component: PlanetaryresourcesHomeComponent;
  let fixture: ComponentFixture<PlanetaryresourcesHomeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PlanetaryresourcesHomeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PlanetaryresourcesHomeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
