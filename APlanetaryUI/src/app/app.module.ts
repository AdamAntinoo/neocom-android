import { BrowserModule }        from '@angular/platform-browser';
import { NgModule }             from '@angular/core';
import { FormsModule }          from '@angular/forms';
import { HttpModule }           from '@angular/http';
import { RouterModule, Routes } from '@angular/router';
import { AppComponent }         from './app.component';
//--- DIRECTIVES
import { BlurForwarder } from './directives/BlurForwarder.directive';
//--- SERVICES
import { AppCoreDataService }           from './services/app-core-data.service';
import { EveItemService }               from './services/eve-item.service';
import { PlanetaryResourceListService } from './services/planetary-resource-list.service';
//--- COMPONENTS
import { HeaderComponent }                  from './header/header.component';
import { PlanetaryResourcesHomeComponent }  from './planetaryresources-home/planetaryresources-home.component';
import { PRListComponent }                  from './prlist/prlist.component';
import { TransformedResourceListComponent } from './transformedresourcelist/transformedresourcelist.component';
import { ResourceListSelectorComponent }    from './resourcelistselector/resourcelistselector.component';


//import { EveItemComponent }         from './eve-item/eve-item.component';
//import { ListaPlanetaryComponent }  from './lista-planetary/lista-planetary.component';

//--- ROUTES
const appRoutes: Routes = [
  { path: 'home', component: PlanetaryResourcesHomeComponent },
  {
    path: '',
    redirectTo: '/home',
    pathMatch: 'full'
  }
];

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    PlanetaryResourcesHomeComponent,
    PRListComponent,
    TransformedResourceListComponent,
    ResourceListSelectorComponent,
    BlurForwarder
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    RouterModule.forRoot(appRoutes)
  ],
  providers: [
    AppCoreDataService,
    EveItemService,
    PlanetaryResourceListService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
