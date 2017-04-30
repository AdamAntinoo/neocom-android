//	PROJECT:     POC-ASB-Planetary (POC.ASBP)
//	AUTHORS:     Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:   (c) 2017 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT: Angular
//	DESCRIPTION: Stores the interchange date for the application until I know a better way to share that
// information.

import { Injectable } from '@angular/core';
//--- SERVICES
import { PlanetaryResourceListService } from '../services/planetary-resource-list.service';
//--- MODELS
import { PlanetaryResource } from '../models/PlanetaryResource';
import { ResourceList }      from '../models/ResourceList';

@Injectable()
export class AppCoreDataService {
  //--- Application Globals
  private title: string = "<TITLE>";
  private version: string = "<VERSION>";
  private transformList: PlanetaryResource[] = [];
  private currentResourceList: ResourceList;
  private currentResources: PlanetaryResource[] = [];

  constructor(private resourceListService: PlanetaryResourceListService) { }

  public getCurrentResourceList() {
    if (undefined == this.currentResourceList) {
      // Create the default resource list and set it up.
      this.currentResourceList = new ResourceList({ id: 0.0, name: "-Default-" });
    }
    return this.currentResourceList;
  }
  public setCurrentResourceList(target: ResourceList) {
    this.currentResourceList = target;
  }
  public getCurrentResources() {
    return this.currentResources;
  }
  /**
  // Set the new transformed list from the data receoved from another component after a new resource is added. The data returned by the backgrund to the initial component is relayed to this component for display.
  */
  public updateTransformList(newlist: PlanetaryResource[]) {
    this.transformList = newlist;
  }

  public setTitle(newtitle: string) {
    this.title = newtitle;
  }
  public getTitle(): string {
    return this.title;
  }
  public getVersion(): string {
    return this.version;
  }
  public setVersion(newversion: string) {
    this.version = newversion;
  }
}
