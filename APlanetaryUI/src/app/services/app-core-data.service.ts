//	PROJECT:     POC-ASB-Planetary (POC.ASBP)
//	AUTHORS:     Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:   (c) 2017 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT: Angular
//	DESCRIPTION: Stores the interchange date for the application until I know a better way to share that
// information.

import { Injectable } from '@angular/core';
//--- HTTP PACKAGE
import { Http }                               from '@angular/http';
import { Response, Headers, RequestOptions }  from '@angular/http';
import { Observable }                         from 'rxjs/Rx';
import { EveItem }                            from '../models/eve-item';
// Import RxJs required methods
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
//--- SERVICES
//import { PlanetaryResourceListService } from '../services/planetary-resource-list.service';
//--- MODELS
import { PlanetaryResource } from '../models/PlanetaryResource';
import { ResourceList }      from '../models/ResourceList';

@Injectable()
export class AppCoreDataService {
  //--- Application Globals
  static APPLICATION_SERVICE_PORT = "9000";
  static RESOURCE_SERVICE_URL: string = "http://localhost:" + AppCoreDataService.APPLICATION_SERVICE_PORT + "/api/v1";
  //--- Application variables
  private title: string = "<TITLE>";
  private version: string = "<VERSION>";
  private currentScenario: ResourceList;
  private currentResources: PlanetaryResource[] = [];
  private transformedResources: PlanetaryResource[] = [];

  constructor(private http: Http/*private resourceListService: PlanetaryResourceListService*/) { }

  // Returns the current list of resources that belongs to the current selected list.
  public getCurrentResources() {
    //    console.log(">>[AppCoreDataService.getCurrentResources]");
    return this.currentResources;
  }
  // Selects a list as the current list. This generates the events to retrieve the resource list form the backend and populates the list of resources.
  public setScenario(target: ResourceList) {
    console.log(">>[AppCoreDataService.setScenario]");
    this.currentScenario = target;
    console.log("--[AppCoreDataService.setScenario]> firing retrieval of list resources");
    this.http.get(AppCoreDataService.RESOURCE_SERVICE_URL + "/resourcelist/id/" + this.currentScenario.getId())
      .map(res => res.json())
      .subscribe(result => {
        console.log("--[AppCoreDataService.setScenario.subscribe]> received: " + result.lenght + " resources");
        this.currentResources = [];
        result.forEach(resource => {
          let pr = new PlanetaryResource(resource);
          this.currentResources.push(pr);
        })
      });
    console.log("<<[AppCoreDataService.setScenario]");
  }
  public getCurrentResourceList() {
    if (undefined == this.currentScenario) {
      // Create the default resource list and set it up.
      this.currentScenario = new ResourceList({ id: 0.0, name: "-Default-" });
    }
    return this.currentScenario;
  }
  // Set the new transformed list from the data receoved from another component after a new resource is added. The data returned by the backgrund to the initial component is relayed to this component for display.
  public getScenario() {
    return this.getCurrentResourceList();
  }
  // Update the contents of the transformation resource list for the data returned when a new resurce is added to the current list.
  public updateTransformList(newlist: PlanetaryResource[]) {
    console.log("><[AppCoreDataService.updateTransformList]");
    this.transformedResources = newlist;
  }
  public getTransformedResources() {
    return this.transformedResources;
  }
  // Adds a new created resource to the current list of resources to keep it in synch with the backend data
  public addResource2List(newResource: PlanetaryResource) {
    this.currentResources.push(newResource);
  }

  //**************************************************************************************

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
