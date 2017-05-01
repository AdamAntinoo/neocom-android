import { Component, OnInit } from '@angular/core';
import { Injectable }        from '@angular/core';
import { PlanetaryResource } from '../models/PlanetaryResource';

@Component({
  selector: 'app-transformedresourcelist',
  templateUrl: './transformedresourcelist.component.html',
  styleUrls: ['./transformedresourcelist.component.css']
})

export class TransformedResourceListComponent implements OnInit {
  public transformList: PlanetaryResource[] = [];

  constructor() { }

  ngOnInit() {
  }
  /**
  // Set the new transformed list from the data receoved from another component after a new resource is added. The data returned by the backgrund to the initial component is relayed to this component for display.
  */
  public updateTransformList(newlist: PlanetaryResource[]) {
    this.transformList = newlist;
  }
}
