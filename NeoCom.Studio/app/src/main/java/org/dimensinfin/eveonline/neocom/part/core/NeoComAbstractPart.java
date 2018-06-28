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

import org.dimensinfin.android.mvc.core.AbstractAndroidPart;
import org.dimensinfin.android.mvc.interfaces.IAndroidPart;
import org.dimensinfin.core.model.Separator;
import org.dimensinfin.eveonline.neocom.model.EveLocation;
import org.dimensinfin.eveonline.neocom.model.NeoComNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.HashMap;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public abstract class NeoComAbstractPart<T extends NeoComNode> extends AbstractAndroidPart {
	// - S T A T I C - S E C T I O N ..........................................................................
	public static Logger logger = LoggerFactory.getLogger("NeoComAbstractPart");

	// --- F O R M A T T E R S
	public static DecimalFormat qtyFormatter = new DecimalFormat("###,##0");
	public static DecimalFormat securityFormatter = new DecimalFormat("0.0");
	public static DecimalFormat itemCountFormatter = new DecimalFormat("###,##0");
	public static DecimalFormat percentageZeroFormat = new DecimalFormat("#0%");
	public static DecimalFormat priceFormatter = new DecimalFormat("###,###.00");
	public static HashMap<Integer, String> securityLevels = new HashMap<Integer, String>();

	static {
		NeoComAbstractPart.securityLevels.put(10, "#2FEFEF");
		NeoComAbstractPart.securityLevels.put(9, "#48F0C0");
		NeoComAbstractPart.securityLevels.put(8, "#00EF47");
		NeoComAbstractPart.securityLevels.put(7, "#00F000");
		NeoComAbstractPart.securityLevels.put(6, "#8FEF2F");
		NeoComAbstractPart.securityLevels.put(5, "#EFEF00");
		NeoComAbstractPart.securityLevels.put(4, "#D77700");
		NeoComAbstractPart.securityLevels.put(3, "#F06000");
		NeoComAbstractPart.securityLevels.put(2, "#F04800");
		NeoComAbstractPart.securityLevels.put(1, "#D73000");
		NeoComAbstractPart.securityLevels.put(0, "#F00000");
	}

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

	public static String upperCaseFirst( String value ) {
		// Convert String to char array.
		char[] array = value.toCharArray();
		// Modify first element in array.
		array[0] = Character.toUpperCase(array[0]);
		// Return string.
		return new String(array);
	}

	// - F I E L D - S E C T I O N ............................................................................
	private Separator.ESeparatorType _themeColor = Separator.ESeparatorType.LINE_WHITE;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public NeoComAbstractPart( NeoComNode model ) {
		super(model);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public T getCastedModel() {
		return (T) this.getModel();
	}

	// --- P A N E L   C O L O R   T H E M E
	public Separator.ESeparatorType getThemeColor() {
		return _themeColor;
	}

	public IAndroidPart setThemeColor( final Separator.ESeparatorType themeColor ) {
		this._themeColor = themeColor;
		return this;
	}

	@Deprecated
	protected Spanned colorFormatLocation( final EveLocation location ) {
		StringBuffer htmlLocation = new StringBuffer();
		double security = location.getSecurityValue();
		htmlLocation.append(this.generateSecurityColor(security, NeoComAbstractPart.securityFormatter.format(security)));
		htmlLocation.append(" ").append(location.getRegion()).append("-").append(location.getStation());
		return Html.fromHtml(htmlLocation.toString());
	}
}
// - UNUSED CODE ............................................................................................
//[01]
