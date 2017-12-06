//	PROJECT:        EVEIndustrialist (EVEI)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2014 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API11.
//	DESCRIPTION:		Application helper for Eve Online Industrialists. Will help on Industry and Manufacture.

package org.dimensinfin.eveonline.neocom.part;

// - IMPORT SECTION .........................................................................................

import android.text.Html;
import android.text.Spanned;

import org.dimensinfin.android.mvc.core.AbstractAndroidPart;
import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.android.mvc.interfaces.IPart;
import org.dimensinfin.android.mvc.part.SeparatorPart;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.core.model.Separator;
import org.dimensinfin.eveonline.neocom.model.EveLocation;
import org.dimensinfin.eveonline.neocom.model.NeoComCharacter;
import org.dimensinfin.eveonline.neocom.model.NeoComNode;
import org.dimensinfin.eveonline.neocom.render.DefaultRender;
import org.dimensinfin.eveonline.neocom.storage.NeoComModelStore;
import org.joda.time.Instant;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Logger;

// - CLASS IMPLEMENTATION ...................................................................................
public abstract class NeoComAbstractPart extends AbstractAndroidPart {
	// - S T A T I C - S E C T I O N ..........................................................................
	protected static Logger										logger										= Logger.getLogger("NeoComAbstractPart");
	private static final long									serialVersionUID					= -2988276062110424930L;
	private static final long									ONEMINUTE									= 60 * 1000;
	private static final long									ONEHOUR										= 60 * NeoComAbstractPart.ONEMINUTE;
	private static final long									ONEDAY										= 24 * NeoComAbstractPart.ONEHOUR;
	protected static DecimalFormat						keyFormatter							= new DecimalFormat("0000000");
	protected static DecimalFormat						priceFormatter						= new DecimalFormat("###,###.00");
	protected static DecimalFormat						qtyFormatter							= new DecimalFormat("###,##0");
	protected static DecimalFormat moduleIndexFormatter			= new DecimalFormat("000");
	protected static DecimalFormat						moduleMultiplierFormatter	= new DecimalFormat("x##0.0");
	protected static DecimalFormat						itemCountFormatter				= new DecimalFormat("###,##0");
	protected static DecimalFormat						iskFormatter							= new DecimalFormat("###,##0.0# MISK");
	protected static DecimalFormat						volumeFormatter						= new DecimalFormat("###,##0.0");
	protected static DecimalFormat						securityFormatter					= new DecimalFormat("0.0");
	protected static HashMap<Integer, String> securityLevels						= new HashMap<Integer, String>();
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

	//	protected final DateTimeFormatter timePointFormatter = DateTimeFormat.forPattern("yyyy/MMM/dd HH:mm");
	protected static final DateTimeFormatter timePointFormatter = DateTimeFormat.forPattern("YYYY.MM.dd HH:mm");
	// - F I E L D - S E C T I O N ............................................................................

	public static String generateTimeString(final long millis) {
		try {
			DateTimeFormatterBuilder timeFormatter = new DateTimeFormatterBuilder();
			if (millis > NeoComAbstractPart.ONEDAY) {
				timeFormatter.appendDayOfYear(1).appendLiteral("D ");
			}
			if (millis > NeoComAbstractPart.ONEHOUR) {
				timeFormatter.appendHourOfDay(2).appendLiteral(":");
			}
			if (millis > NeoComAbstractPart.ONEMINUTE) {
				timeFormatter.appendMinuteOfHour(2).appendLiteral(":").appendSecondOfMinute(2);
			}
			return timeFormatter.toFormatter().print(new Instant(millis));
		} catch (RuntimeException rtex) {
			return "0:00";
		}
	}

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public NeoComAbstractPart (final ICollaboration model) {
		super( model);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	/**
	 * The goal of this method is to return the list of Parts on the children list that should be visible and
	 * that collaborates to the ListView population. The default behavior for this method is to check if the
	 * Model behind the Part is expanded, in that case the children have the opportunity to be added to the
	 * visible list.<br>
	 * If the Region is not expanded the list of elements collaborated should be:
	 * <ul>
	 * <li>Region</li>
	 * </ul>
	 * If the Region is expanded and has a number of contents the collaboration changes to add the Separators:
	 * <ul>
	 * <li>Separator Red</li>
	 * <li>Region</li>
	 * <li>Location...</li>
	 * <li>Separator Red</li>
	 * </ul>
	 */
	@Override
	public ArrayList<IPart> collaborate2View() {
		ArrayList<IPart> result = new ArrayList<IPart>();
		// Check if the Part is visible and collaborates to the LiastView.
	//	if (this.isVisible()) {
			// First we have to check if we have contents or if the part should be shown altogether.
			//			int contentCount = this.getCastedModel().getContentCount();
			// If the node is expanded then give the children the opportunity to also be added.
			if (this.isExpanded()) {
				if (this.isEmpty()) {
					if (this.isRenderWhenEmpty()) {
						// The region is surrounded with the Red Separator at the beginning and at the end.
						result.add(new SeparatorPart(new Separator().setType(Separator.ESeparatorType.LINE_RED))
								.setRenderMode(this.getRenderMode()));
						result.add(this);
						// This region is empty of contents. Add the Empty Separator and complete.
						result.add(new SeparatorPart(new Separator().setType(Separator.ESeparatorType.EMPTY_SIGNAL))
								.setRenderMode(this.getRenderMode()));
						result.add(new SeparatorPart(new Separator().setType(Separator.ESeparatorType.LINE_RED))
								.setRenderMode(this.getRenderMode()));
					}
				} else {
					// Generate the list of Parts that should be collaborated to the ListView.
					// The region is surrounded with the Red Separator at the beginning and at the end.
					result.add(
							new SeparatorPart(new Separator().setType(Separator.ESeparatorType.LINE_RED)).setRenderMode(this.getRenderMode()));
					result.add(this);
					// --- This is the section where we get the Contents and then we apply the customized Part policies.
					Vector<IPart> childParts = this.runPolicies(this.getChildren());
					// --- End of policies
					for (IPart part : childParts) {
						// For each of the children that are registered to this part and that have a model recursively search for collaborations.
						ArrayList<IPart> collaboration = part.collaborate2View();
						if (!part.isNewImplemented()) {
							result.add(part);
						}
						result.addAll(collaboration);
					}
					result.add(
							new SeparatorPart(new Separator().setType(Separator.ESeparatorType.LINE_RED)).setRenderMode(this.getRenderMode()));
				}
			} else {
				// Check for items that will not shown when empty.
				if (this.isRenderWhenEmpty()) {
					result.add(this);
				}
			}
	//	}
		return result;
	}
	public NeoComNode getCastedModel() {
		return (NeoComNode) this.getModel();
	}

	protected Spanned colorFormatLocation(final EveLocation location) {
		StringBuffer htmlLocation = new StringBuffer();
		double security = location.getSecurityValue();
		htmlLocation.append(this.generateSecurityColor(security, NeoComAbstractPart.securityFormatter.format(security)));
		htmlLocation.append(" ").append(location.getRegion()).append("-").append(location.getStation());
		return Html.fromHtml(htmlLocation.toString());
	}

	protected String generateDateString(final long millis) {
		try {
			DateTimeFormatterBuilder dateFormatter = new DateTimeFormatterBuilder();
			dateFormatter.appendYear(4, 4).appendLiteral(".");
			dateFormatter.appendMonthOfYear(2).appendLiteral(".");
			dateFormatter.appendDayOfMonth(2).appendLiteral(" ");
			dateFormatter.appendHourOfDay(2).appendLiteral(":");
			dateFormatter.appendMinuteOfHour(2).appendLiteral(":").appendSecondOfMinute(2);
			return dateFormatter.toFormatter().print(new Instant(millis));
		} catch (RuntimeException rtex) {
			return "2014 01/01 00:00:00";
		}
	}
	/**
	 * The default actions inside this method usually are the sorting of the children nodes. For actions there
	 * is not sorting required so do nothing with the input.
	 */
	@Override
	public Vector<IPart> runPolicies(final Vector<IPart> targets) {
		return targets;
	}

	/**
	 * Accepts a <code>Duration</code> as a parameter and returns an string with the formatted duration in the
	 * pattern <b>9D 99H 99M 99S</b> depending on the real duration of the object.
	 * 
	 * @return
	 */
	protected String generateDurationString(final Instant timer) {
		return this.generateDurationString(timer.getMillis());
	}

	protected String generateDurationString(final long millis) {
		DateTimeFormatterBuilder timeLeftCountdown = new DateTimeFormatterBuilder();
		if (millis > NeoComAbstractPart.ONEDAY) {
			timeLeftCountdown.appendDayOfYear(1).appendLiteral("D ");
		}
		if (millis > NeoComAbstractPart.ONEHOUR) {
			timeLeftCountdown.appendHourOfDay(2).appendLiteral("H ");
		}
		if (millis > NeoComAbstractPart.ONEMINUTE) {
			timeLeftCountdown.appendMinuteOfHour(2).appendLiteral("M ").appendSecondOfMinute(2).appendLiteral('S');
		}
		return timeLeftCountdown.toFormatter().print(new Instant(millis));
	}

	protected String generatePriceString(final double price, final boolean compress, final boolean addSuffix) {
		// Generate different formats depending on the quantity and the compress flag.
		// Get rid of negative numbers.
		if (compress) {
			if (Math.abs(price) > 1200000000.0) if (addSuffix)
				return NeoComAbstractPart.priceFormatter.format(price / 1000.0 / 1000.0 / 1000.0) + " B ISK";
			else
				return NeoComAbstractPart.priceFormatter.format(price / 1000.0 / 1000.0 / 1000.0);
			if (Math.abs(price) > 12000000.0) if (addSuffix)
				return NeoComAbstractPart.priceFormatter.format(price / 1000.0 / 1000.0) + " M ISK";
			else
				return NeoComAbstractPart.priceFormatter.format(price / 1000.0 / 1000.0);
		}
		if (addSuffix)
			return NeoComAbstractPart.priceFormatter.format(price) + " ISK";
		else
			return NeoComAbstractPart.priceFormatter.format(price);
	}

	protected String generateSecurityColor(double sec, final String data) {
		StringBuffer htmlFragmentWithColor = new StringBuffer();
		String secColor = "#F00000";
		// Get the color from the table.
		if (sec < 0.0) {
			sec = 0.0;
		}
		if (sec > 1.0) {
			sec = 1.0;
		}
		long secAdjust = Long.valueOf(Math.round(sec * 10.0)).intValue();
		secColor = NeoComAbstractPart.securityLevels.get(Long.valueOf(secAdjust).intValue());
		htmlFragmentWithColor.append("<font color='").append(secColor).append("'>").append(data).append("</font>");
		return htmlFragmentWithColor.toString();
	}

	protected NeoComCharacter getPilot() {
		return NeoComModelStore.getSingleton().getPilot();
	}

	@Override
	protected AbstractRender selectRenderer() {
		return new DefaultRender(this, _activity);
	}
}

// - UNUSED CODE ............................................................................................
