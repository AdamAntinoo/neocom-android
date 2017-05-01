import { PlanetaryResource } from './planetary-resource';

describe('PlanetaryResource', () => {
  it('should create an instance', () => {
    expect(new PlanetaryResource()).toBeTruthy();
  });

  it('should accept values in the constructor', () => {
    let pr = new PlanetaryResource({typeid: 9838,
     quantity: 123.0,
     name: "Planetary Resource"
    });
    expect(pr.quantity).toEqual(123.0);
    expect(pr.name).toEqual("Planetary Resource");
  });
});
