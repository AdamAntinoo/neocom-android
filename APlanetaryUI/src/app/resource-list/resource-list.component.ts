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
    return this.appService.getCurrentResources();
  }
}
