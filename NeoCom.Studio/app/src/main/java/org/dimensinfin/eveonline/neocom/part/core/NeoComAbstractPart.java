//  PROJECT:     NeoCom.Android (NEOC.A)
//  AUTHORS:     Adam Antinoo - adamantinoo.git@gmail.com
//  COPYRIGHT:   (c) 2013-2018 by Dimensinfin Industries, all rights reserved.
//  ENVIRONMENT: Android API22.
//  DESCRIPTION: Android Application related to the Eve Online game. The purpose is to download and organize
//               the game data to help capsuleers organize and prioritize activities. The strong points are
//               help at the Industry level tracking and calculating costs and benefits. Also the market
//               information update service will help to identify best prices and locations.
//               Planetary Interaction and Ship fittings are point under development.
//               ESI authorization is a new addition that will give continuity and allow download game data
//               from the new CCP data services.
//               This is the Android application version but shares libraries and code with other application
//               designed for Spring Boot Angular 4 platform.
//               The model management is shown using a generic Model View Controller that allows make the
//               rendering of the model data similar on all the platforms used.
package org.dimensinfin.eveonline.neocom.part.core;

import android.text.Html;
import android.text.Spanned;

import java.text.DecimalFormat;
import java.util.Hashtable;
import java.util.Hashtable;
import java.util.Hashtable;

import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.model.EveLocation;
import org.dimensinfin.eveonline.neocom.part.core.NeoComAbstractPartOld;
import org.joda.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public abstract class NeoComAbstractPart extends NeoComAbstractPartOld {
	// - S T A T I C - S E C T I O N ..........................................................................
	public static Logger logger = LoggerFactory.getLogger("NeoComAbstractPart");

	// --- F O R M A T T E R S
	public static DecimalFormat qtyFormatter = new DecimalFormat("###,##0");
	public static DecimalFormat securityFormatter = new DecimalFormat("0.0");

	// --- S T A T I C   M O D E L   D A T A   T R A N S F O R M E R S
	public static String generatePriceString( final double price, final boolean compress, final boolean addSuffix ) {
		// Generate different formats depending on the quantity and the compress flag.
		// Get rid of negative numbers.
		if ( compress ) {
			if ( Math.abs(price) > 1200000000.0 ) if ( addSuffix )
				return NeoComAbstractPart.priceFormatter.format(price / 1000.0 / 1000.0 / 1000.0) + " B ISK";
			else
				return NeoComAbstractPart.priceFormatter.format(price / 1000.0 / 1000.0 / 1000.0);
			if ( Math.abs(price) > 12000000.0 ) if ( addSuffix )
				return NeoComAbstractPart.priceFormatter.format(price / 1000.0 / 1000.0) + " M ISK";
			else
				return NeoComAbstractPart.priceFormatter.format(price / 1000.0 / 1000.0);
		}
		if ( addSuffix )
			return NeoComAbstractPart.priceFormatter.format(price) + " ISK";
		else
			return NeoComAbstractPart.priceFormatter.format(price);
	}
	public static String generateSecurityColor( double sec, final String data ) {
		StringBuffer htmlFragmentWithColor = new StringBuffer();
		String secColor = "#F00000";
		// Get the color from the table.
		if ( sec < 0.0 ) {
			sec = 0.0;
		}
		if ( sec > 1.0 ) {
			sec = 1.0;
		}
		long secAdjust = Long.valueOf(Math.round(sec * 10.0)).intValue();
		secColor = NeoComAbstractPart.securityLevels.get(Long.valueOf(secAdjust).intValue());
		htmlFragmentWithColor.append("<font color='").append(secColor).append("'>").append(data).append("</font>");
		return htmlFragmentWithColor.toString();
	}

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public NeoComAbstractPart( ICollaboration model ) {
		super(model);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	@Deprecated
	protected Spanned colorFormatLocation( final EveLocation location ) {
		StringBuffer htmlLocation = new StringBuffer();
		double security = location.getSecurityValue();
		htmlLocation.append(this.generateSecurityColor(security, NeoComAbstractPart.securityFormatter.format(security)));
		htmlLocation.append(" ").append(location.getRegion()).append("-").append(location.getStation());
		return Html.fromHtml(htmlLocation.toString());
	}
//	@Override
//	public String toString() {
//		StringBuffer buffer = new StringBuffer("NeoComAbstractPart [ ");
//		buffer.append("name: ").append(0);
//		buffer.append("]");
//		buffer.append("->").append(super.toString());
//		return buffer.toString();
//	}
}
// - UNUSED CODE ............................................................................................
//[01]
