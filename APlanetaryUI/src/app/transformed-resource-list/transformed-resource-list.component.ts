import { Component }          from '@angular/core';
import { OnInit }             from '@angular/core';
//--- SERVICES
import { AppCoreDataService } from '../services/app-core-data.service';

@Component({
  selector: 'app-transformed-resource-list',
  templateUrl: './transformed-resource-list.component.html',
  styleUrls: ['./transformed-resource-list.component.css']
})
export class TransformedResourceListComponent implements OnInit {

  constructor(private appService: AppCoreDataService) { }

  ngOnInit() {
  }

  public getTransformedResources() {
    return this.appService.getTransformedResources();
  }
}
