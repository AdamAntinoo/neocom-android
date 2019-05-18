package org.dimensinfin.eveonline.neocom.mining;

import java.util.List;
import java.util.Objects;

import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetUniverseTypesTypeIdOk;
import org.dimensinfin.eveonline.neocom.sharedcode.provider.NeoComAndroid;

/**
 * An <code>ItemPack</code> is a stack of items of a single type. It is similar to a Resource but a more simple instance that only contains
 * the item data, a quantity and the item price information and a stub where to connect the market data if requested.
 * The item default price is set to the price the item has on the ESI market data.
 *
 * A more detailed and adjusted price can be obtained processing the market data with the forecaster and then getting the lowest buy price at Jita.
 */
public class ItemPack implements IAggregableItem {
	private GetUniverseTypesTypeIdOk resource;
	private double price = 0.0;
	private long quantity = 0;

	// - I A G G R E G A B L E I T E M
	@Override
	public long getQuantity() {
		return this.quantity;
	}

	@Override
	public double getVolume() {
		return this.resource.getVolume() * this.quantity;
	}

	@Override
	public double getPrice() {
		return 0.0;
	}

	// - I C O L L A B O R A T I O N
	@Override
	public List<ICollaboration> collaborate2Model( final String variation ) {
		return null;
	}

	// - C O M P A R A B L E
	@Override
	public int compareTo( final Object o ) {
		return 0;
	}

	// -  B U I L D E R
	public static class Builder {
		private ItemPack onConstruction;

		public Builder() {
			this.onConstruction = new ItemPack();
		}

		public Builder withResourceType( final int resourceType ) {
			this.onConstruction.resource = NeoComAndroid.getSingleton().getEveItemProvider().search(resourceType);
			return this;
		}

		public Builder withQuantity( final long quantity ) {
			this.onConstruction.quantity = quantity;
			return this;
		}

		public ItemPack build() {
			Objects.requireNonNull(this.onConstruction.resource);
			// Search for a tentative initial price.
			this.onConstruction.price = NeoComAndroid.getSingleton().getEveItemProvider().getPrice(this.onConstruction.resource.getTypeId());
			return this.onConstruction;
		}
	}
}
