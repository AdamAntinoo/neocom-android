// PROJECT:     POC-ASB-Planetary (POC.ASBP)
// AUTHORS:     Adam Antinoo - adamantinoo.git@gmail.com
// COPYRIGHT:   (c) 2017 by Dimensinfin Industries, all rights reserved.
// ENVIRONMENT: Angular - CLASS
// DESCRIPTION: Generic Eve Resource. Han a point to the EveItem and the quantity of the resource stored.
import { EveItem } from '../models/eve-item';

export class Resource {
  private item: EveItem;
  private resourceID: number = -1.0;
  private baseQty: number = 0.0;
  private stackSize: number = 1.0;
  private damage: number = 1.0;
  private registrationDate;
}
