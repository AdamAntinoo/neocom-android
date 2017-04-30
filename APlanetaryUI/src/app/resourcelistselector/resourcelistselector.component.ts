import { Component, OnInit } from '@angular/core';
//--- DIRECTIVES
import { BlurForwarder } from '../directives/BlurForwarder.directive';
//--- SERVICES
import { AppCoreDataService }           from '../services/app-core-data.service';
import { PlanetaryResourceListService } from '../services/planetary-resource-list.service';
import { ResourceList }                 from '../models/ResourceList';

@Component({
  selector: 'app-resourcelistselector',
  host: { '(input-blur)': 'onInputBlur($event)' },
  templateUrl: './resourcelistselector.component.html',
  styleUrls: ['./resourcelistselector.component.css']
})

export class ResourceListSelectorComponent implements OnInit {
  private rlidsequence = 0.0;
  private rlLists: ResourceList[] = [];
  private currentResourceList: ResourceList;
  private listName: string;
  private isEnteringNewList: boolean = false;

  constructor(private resourceListService: PlanetaryResourceListService, private appDataService: AppCoreDataService) { }

  ngOnInit() {
    console.log(">>[ResourcelistselectorComponent.ngOnInit]");
    // Pre inititialize the list of resources.
    if (this.rlLists.length < 1) {
      let newrl = new ResourceList({ id: 0.0, name: "-Default-" });
      this.rlLists.push(newrl);
      this.currentResourceList = newrl;
      this.appDataService.setCurrentResourceList(this.currentResourceList);
    }
    console.log("--[ResourcelistselectorComponent.ngOnInit]> Preinitialize the list: " + JSON.stringify(this.rlLists));

    // Download the list of Resource Lists that are stored on the database.
    console.log("--[ResourcelistselectorComponent.ngOnInit]> firing getAllResourceLists"); this.resourceListService.getAllResourceLists()
      .subscribe(result => {
        console.log("--[ResourcelistselectorComponent.ngOnInit.subscribe]> resource lists: " + JSON.stringify(result));
        // The the list of planetary resource lists to the data returned.
        this.setResourceLists(result);
      });

    console.log("<<[ResourcelistselectorComponent.ngOnInit]");
  }
  onInputBlur(event) {
    console.log(">>[ResourcelistselectorComponent.onInputBlur]");
    let name = event.target.value;
    console.log("--[ResourcelistselectorComponent.onInputBlur]> name value: " + name);
    this.newResourceList(name);
  }
  public openNewList() {
    this.isEnteringNewList = true;
  }
  // Check if the name introduced is not already matched as an available resource list. In that case we create a new list and set it up as the default list. This default llist is stored on the data service to be used by the other components.
  private newResourceList(newname) {
    console.log(">>[ResourcelistselectorComponent.newResourceList]> newname: " + newname);
    this.rlLists.forEach(rl => {
      if (newname == rl.getName()) {
        // Make this the current resource list but do not create a new one.
        console.log(">>[ResourcelistselectorComponent.newResourceList]> match found. Changing list.");
        this.setDefaultList(newrl);
        return;
      }
    })
    console.log(">>[ResourcelistselectorComponent.newResourceList]> creating new list.");
    this.rlidsequence++;
    let newrl = new ResourceList();
    newrl.id = this.rlidsequence;
    newrl.setName(newname);
    console.log(">>[ResourcelistselectorComponent.newResourceList]> newrl: " + JSON.stringify(newrl));
    //  this.rlLists.push(newrl);
    // Set this new list as the default list.
    //  this.setDefaultList(newrl);
    // Create the list on the backend data service.
    this.resourceListService.newResourceList(newrl)
      .subscribe(result => {
        console.log(">>[ResourcelistselectorComponent.newResourceList.subscribe]> result: " + JSON.stringify(result));
        // Set the list to the new list.
        this.setResourceLists(result);
        this.isEnteringNewList = false;
        this.setDefaultList(newrl);
      })
  }
  private setResourceLists(newdata: ResourceList[]) {
    // If the size of the list is 0 then we should create the default list where to assign all resources until the user sets a new list name.
    if (newdata.length < 1) {
      let newrl = new ResourceList({ id: 0.0, name: "-Default-" });
      this.rlLists.push(newrl);
      this.setDefaultList(newrl);
    } else {
      // We should get the first element and set it as the current resource list.
      this.rlLists = newdata;
      this.setDefaultList(this.rlLists[0]);
    }
  }
  private setDefaultList(targetList: ResourceList) {
    this.currentResourceList = targetList;
    this.appDataService.setCurrentResourceList(targetList);
    this.listName = this.currentResourceList.getName();
  }
  private updateListIdSequence() {
    for (let list of this.rlLists) {
      if (list.getId() > this.rlidsequence) this.rlidsequence = list.getId();
    }
    console.log("<<[ResourcelistselectorComponent.updateListIdSequence]> rlidsequence: " + this.rlidsequence);
  }
}
