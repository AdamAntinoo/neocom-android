import { Injectable }                         from '@angular/core';
import { Http }                               from '@angular/http';
import { Response, Headers, RequestOptions }  from '@angular/http';
//import { URLSearchParams, Jsonp }             from '@angular/http';
import { Observable }                         from 'rxjs/Rx';
import { EveItem }                            from '../models/eve-item';
// Import RxJs required methods
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';

import { PlanetaryResource } from '../models/PlanetaryResource';
import { ResourceList } from '../models/ResourceList';

@Injectable()
export class PlanetaryResourceListService {
  static APPLICATION_SERVICE_PORT = "9000";
  static RESOURCE_SERVICE_URL: string = "http://localhost:" + PlanetaryResourceListService.APPLICATION_SERVICE_PORT + "/api/v1";

  private lastId: number = 0;
  private resourceLists: ResourceList[] = [];

  constructor(private http: Http) { }

  public addResource2List(listid: number, newResource: PlanetaryResource) {
    let prListData = { id: name, data: newResource };
    return this.http.post(PlanetaryResourceListService.RESOURCE_SERVICE_URL + "/newresource/", prListData)
      .map(res => res.json())
      .map(result => {
        console.log("--[PlanetaryResourceListService.addResource2List.map]> result: " + JSON.stringify(result));
        return result;
      });
    //    .catch(res => Observable.throw(res.json()));
  }
  public deleteResource(listid: number, resourceId: number) {
    console.log(">>[PlanetaryResourceListService.deleteResource]")
    console.log("--[PlanetaryResourceListService.deleteResource]> listid: " + listid)
    console.log("--[PlanetaryResourceListService.deleteResource]> resourceId: " + resourceId)
    return this.http.get(PlanetaryResourceListService.RESOURCE_SERVICE_URL + "/deleteResource/" + listid + "/" + resourceId)
      .map(res => res.json())
      .map(result => {
        console.log("--[PlanetaryResourceListService.getPRList.map]> result: " + JSON.stringify(result));
        return result;
      });
  }
  /**********************************************************************************************/

  public addResourceList(newlist: ResourceList): PlanetaryResourceListService {
    if (!newlist.id) {
      newlist.id = ++this.lastId;
    }
    this.resourceLists.push(newlist);
    return this;
  }
  public deleteTodoById(id: number): PlanetaryResourceListService {
    this.resourceLists = this.resourceLists
      .filter(rl => rl.id !== id);
    return this;
  }
  public updateTodoById(id: number, values: Object = {}): ResourceList {
    let rl = this.getResourceListById(id);
    if (!rl) {
      return null;
    }
    Object.assign(rl, values);
    return rl;
  }
  public getAllResourceLists() {
    return this.http.get(PlanetaryResourceListService.RESOURCE_SERVICE_URL + "/resourcelists")
      .map(res => res.json())
      .map(result => {
        console.log("--[PlanetaryResourceListService.getAllResourceLists.map]> result: " + JSON.stringify(result));
        for (let rl of result) {
          let newrl = new ResourceList(rl);
          this.resourceLists.push(newrl);
        }
        return this.resourceLists;
      });
  }
  private getResourceListById(id: number): ResourceList {
    return this.resourceLists
      .filter(rl => rl.id === id)
      .pop();
  }

  /*************************************************/
  public getPRList(name: string) {
    // Read from the backend the list with this name.
    return this.http.get(PlanetaryResourceListService.RESOURCE_SERVICE_URL + "/resourcelists/" + name)
      .map(res => res.json())
      .map(result => {
        console.log("--[PlanetaryResourceListService.getPRList.map]> result: " + JSON.stringify(result));
        return result;
      });
  }
  // public savePRList(name: string, newList: PlanetaryResource[]) {
  //   let prListData = { name: name, data: newList };
  //   this.http.post("http://localhost:8090/api/v1/addresourcelist", prListData)
  //     .subscribe(function() {
  //       console.log("createProject called successfully")
  //     });
  //   //  .catch((error: any) => Observable.throw(error.json().error || 'Server error'))
  // }
  public searchTypeName(id: number) {
    return this.http.get(PlanetaryResourceListService.RESOURCE_SERVICE_URL + "/eveitem/" + id)
      .map(res => res.json())
      .map(result => {
        console.log("--[PlanetaryResourceListService.searchTypeName.map]> result: " + JSON.stringify(result));
        return result;
      });
    //    .catch(res => Observable.throw(res.json()));
  }
}
