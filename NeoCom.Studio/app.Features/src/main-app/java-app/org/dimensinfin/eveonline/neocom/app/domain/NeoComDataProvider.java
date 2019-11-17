package org.dimensinfin.eveonline.neocom.app.domain;

import java.text.DecimalFormat;

public abstract class NeoComDataProvider {
	public static final DecimalFormat qtyFormatter = new DecimalFormat( "###,##0" );
	public static final DecimalFormat priceFormatter = new DecimalFormat( "###,###.00" );

	protected String generatePriceString( final double price ) {
		return this.generatePriceString( price, true, true );
	}

	protected String generatePriceString( final double price, final Boolean compress, final boolean addSuffix ) {
		// Generate different formats depending on the quantity and the compress flag.
		// Get rid of negative numbers.
		if (compress) {
			if (Math.abs( price ) > 1200000000.0)
				if (addSuffix)
					return priceFormatter.format( price / 1000.0 / 1000.0 / 1000.0 ) + " B ISK";
				else
					return priceFormatter.format( price / 1000.0 / 1000.0 / 1000.0 );
			if (Math.abs( price ) > 12000000.0)
				if (addSuffix)
					return priceFormatter.format( price / 1000.0 / 1000.0 ) + " M ISK";
				else
					return priceFormatter.format( price / 1000.0 / 1000.0 );
		}
		if (addSuffix)
			return priceFormatter.format( price ) + " ISK";
		else
			return priceFormatter.format( price );
	}

	public static String generatePriceStringStatic( final double price, final Boolean compress, final boolean addSuffix ) {
		// Generate different formats depending on the quantity and the compress flag.
		// Get rid of negative numbers.
		if (compress) {
			if (Math.abs( price ) > 1200000000.0)
				if (addSuffix)
					return priceFormatter.format( price / 1000.0 / 1000.0 / 1000.0 ) + " B ISK";
				else
					return priceFormatter.format( price / 1000.0 / 1000.0 / 1000.0 );
			if (Math.abs( price ) > 12000000.0)
				if (addSuffix)
					return priceFormatter.format( price / 1000.0 / 1000.0 ) + " M ISK";
				else
					return priceFormatter.format( price / 1000.0 / 1000.0 );
		}
		if (addSuffix)
			return priceFormatter.format( price ) + " ISK";
		else
			return priceFormatter.format( price );
	}
}
