import { Component }                    from '@angular/core';
import { OnInit }                       from '@angular/core';
//--- SERVICES
import { AppCoreDataService }           from '../services/app-core-data.service';
import { PlanetaryResourceListService } from '../services/planetary-resource-list.service';
//--- MODELS
import { PlanetaryResource }            from '../models/PlanetaryResource';
//import { ResourceList }                 from '../models/ResourceList';

@Component({
  selector: 'app-resource-list',
  templateUrl: './resource-list.component.html',
  styleUrls: ['./resource-list.component.css']
})

export class ResourceListComponent implements OnInit {
  private resurceList: PlanetaryResource[] = [];
  private showNewResourceForm: boolean = false;
  private idIsValid: boolean = false;
  private newResource: PlanetaryResource = new PlanetaryResource();

  constructor(private resourceListService: PlanetaryResourceListService, private appService: AppCoreDataService) { }

  ngOnInit() {
  }
  // Go to the application data store service and get the list of resources to display on the table. This removed the requirement to maintain a duplicated list on inside the component and all operations are performed with the data service as a proxy to the underlying backend system.
  public getResourceList() {
    console.log(">>[ResourceListComponent.getResourceList]");
    return this.appService.getCurrentResources();
  }
  /**********************************************************************************************/
  //  T E M P L A T E   M E T H O D S
  /**********************************************************************************************/
  // Template interaction method to show the new resource form.
  public openNewResource() {
    // Open the new resource component to enter the forms data
    this.newResource = new PlanetaryResource();
    this.idIsValid = false;
    this.showNewResourceForm = true;
  }
  /**********************************************************************************************/
  //  F O R M   M E T H O D S
  /**********************************************************************************************/
  // This is the method executed when the user completes the registration of a new resource. It will store the resource on the backend database and reload the data to the main service.
  public onSubmit() {
    console.log(">>[ResourceListComponent.onSubmit]");
    // Check if this resource already exists. If so add both resources quantities. Otherwise add the resource to the resource list.
    // let hit = this.search4id(this.newResource.getTypeid());
    // console.log("--[PlanetaryResourceListService.addResource]> hit: " + JSON.stringify(hit));
    // if (undefined == hit) {
    //   this.prList.push(this.newResource);
    // } else {
    //   let newQty = hit.getQuantity() * 1.0;
    //   newQty = newQty + this.newResource.getQuantity() * 1.0;
    //   hit.setQuantity(newQty);
    // }
    //    let counter = this.prList.length;
    // Hide again the new resource form.
    this.showNewResourceForm = false;

    // Send the new resource to the backend to be added to the current resources list.
    let list = this.appService.getScenario();
    this.resourceListService.addResource2List(list.getId(), this.newResource);
    this.appService.addResource2List(this.newResource);
    // .subscribe(result => {
    //   // console.log("--[PRListComponent.onSubmit.subscribe]> result: " + JSON.stringify(result));
    //   // Clear the list before loading it again with the new list
    //   let transformList = [];
    //   for (let resource of result) {
    //     let trresource = new PlanetaryResource();
    //     trresource.typeid = resource.typeID;
    //     trresource.quantity = resource.quantity;
    //     trresource.name = resource.name;
    //     transformList.push(trresource);
    //   }
    //   // Send the transformed data to the transformed ui component.
    //   this.dataService.updateTransformList(transformList);
    // });
  }
}
