//	PROJECT:        NeoCom.Android (NEOC.A)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2017 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API22.
//	DESCRIPTION:		Application to get access to CCP api information and help manage industrial activities
//									for characters and corporations at Eve Online. This is the Android application but shares
//                  libraries and code with other application designed for alternate platforms.
//                  The model management is shown using a generic Model View Controller that allows make the
//                  rendering of the model data similar on all the platforms used.
package org.dimensinfin.eveonline.neocom.render;

// - IMPORT SECTION .........................................................................................

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import org.dimensinfin.android.mvc.core.AbstractAndroidPart;
import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.connector.NeoComAppConnector;
import org.dimensinfin.eveonline.neocom.constant.AppWideConstants;
import org.dimensinfin.eveonline.neocom.model.EveLocation;
import org.joda.time.Instant;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

import java.text.DecimalFormat;
import java.util.HashMap;

// - CLASS IMPLEMENTATION ...................................................................................
public abstract class NeoComRender extends AbstractRender {
	// - S T A T I C - S E C T I O N ..........................................................................
	protected static final long											ONEMINUTE									= 60 * 1000;
	protected static final long											ONEHOUR										= 60 * NeoComRender.ONEMINUTE;
	protected static final long											ONEDAY										= 24 * NeoComRender.ONEHOUR;
	protected static final DecimalFormat						keyFormatter							= new DecimalFormat("0000000");
	protected static final DateTimeFormatter				durationFormatter					= DateTimeFormat.forPattern("d HH:mm");
	protected static final DateTimeFormatter				timePointFormatter				= DateTimeFormat
			.forPattern("YYYY.MM.dd HH:mm");
	protected static final DateTimeFormatter				jobTimeFormatter					= DateTimeFormat.forPattern("D HH:MM");
	protected static final DecimalFormat						priceFormatter						= new DecimalFormat("###,###.00");
	protected static final DecimalFormat						qtyFormatter							= new DecimalFormat("###,##0");
	protected static final DecimalFormat						moduleIndexFormatter			= new DecimalFormat("000");
	protected static final DecimalFormat						queueIndexFormatter				= new DecimalFormat("00");
	protected static final DecimalFormat						moduleMultiplierFormatter	= new DecimalFormat("x##0.0");
	protected static final DecimalFormat						itemCountFormatter				= new DecimalFormat("###,##0");
	protected static final DecimalFormat						volumeFormatter						= new DecimalFormat("###,##0.0");
	protected static final DecimalFormat						securityFormatter					= new DecimalFormat("0.0");
	protected static final HashMap<Integer, String>	securityLevels						= new HashMap<Integer, String>();
	static {
		NeoComRender.securityLevels.put(10, "#2FEFEF");
		NeoComRender.securityLevels.put(9, "#48F0C0");
		NeoComRender.securityLevels.put(8, "#00EF47");
		NeoComRender.securityLevels.put(7, "#00F000");
		NeoComRender.securityLevels.put(6, "#8FEF2F");
		NeoComRender.securityLevels.put(5, "#EFEF00");
		NeoComRender.securityLevels.put(4, "#D77700");
		NeoComRender.securityLevels.put(3, "#F06000");
		NeoComRender.securityLevels.put(2, "#F04800");
		NeoComRender.securityLevels.put(1, "#D73000");
		NeoComRender.securityLevels.put(0, "#F00000");
	}
	public static Typeface daysFace = Typeface
			.createFromAsset(NeoComAppConnector.getSingleton().getApplicationContext().getAssets(), "fonts/Days.otf");

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public NeoComRender (final AbstractAndroidPart target, final Activity context) {
		super(target, context);
	}

	// - M E T H O D - S E C T I O N ..........................................................................

	@Override
	public void updateContent () {
		super.updateContent();
		// Check if the click is running and then replace the right arrow by an spinner.
		if ( getPart().clickRunning() ) {
			if ( null != _rightArrow ) {
				logger.info("-- [LoginRender.updateContent]> Activating spinner animation.");
				_rightArrow.setImageDrawable(this.getContext().getResources().getDrawable(R.drawable.redprogress70, getContext().getTheme()));
				Animation rotation = AnimationUtils.loadAnimation(getContext(), R.anim.clockwise_rotation);
				rotation.setRepeatCount(Animation.INFINITE);
				_rightArrow.startAnimation(rotation);
				_convertView.invalidate();
			}
		}
	}

	public Typeface getThemeTextFont() {
		return NeoComRender.daysFace;
	}

	protected Spanned colorFormat(final String data, final String color, final String suffix) {
		final StringBuffer htmlFragment = new StringBuffer();
		htmlFragment.append("<font color='").append(color).append("'>");
		htmlFragment.append(data);
		if (null != suffix) {
			htmlFragment.append(suffix);
		}
		htmlFragment.append("</font>");
		return Html.fromHtml(htmlFragment.toString());
	}

	protected Spanned colorFormatLocation(final EveLocation location) {
		final StringBuffer htmlLocation = new StringBuffer();
		final double security = location.getSecurityValue();
		htmlLocation.append(this.generateSecurityColor(security, NeoComRender.securityFormatter.format(security)));
		htmlLocation.append(" ").append(location.getRegion()).append("-").append(location.getName());
		return Html.fromHtml(htmlLocation.toString());
	}

	@Override
	abstract protected void createView();

	/**
	 * The price of the manufacture cost on the output is not tinted with the right color. Use red for cero or
	 * negative costs, orange for less than 10% and white for higher (or maybe green for higher). This code can
	 * be refactored to use ever the same generation code.
	 *
	 * @param cost
	 * @param price
	 * @param suffix
	 * @param compress
	 * @return
	 */
	protected Spanned displayManufactureCost(final double cost, final double price, final boolean compress,
			final boolean suffix) {
		final StringBuffer htmlFragmentWithColor = new StringBuffer();
		final String priceString = this.generatePriceString(cost, compress, suffix);
		String color = "#FFFFFF";
		if (cost >= (price * 0.9)) {
			color = "#FFA500";
		}
		if (cost >= price) {
			color = "#F00000";
		}
		htmlFragmentWithColor.append("<font color='").append(color).append("'>").append(priceString).append("</font>");
		return Html.fromHtml(htmlFragmentWithColor.toString());
	}

	protected String generateDateString(final long millis) {
		try {
			final DateTimeFormatterBuilder dateFormatter = new DateTimeFormatterBuilder();
			dateFormatter.appendYear(4, 4).appendLiteral(".");
			dateFormatter.appendMonthOfYear(2).appendLiteral(".");
			dateFormatter.appendDayOfMonth(2).appendLiteral(" ");
			dateFormatter.appendHourOfDay(2).appendLiteral(":");
			dateFormatter.appendMinuteOfHour(2).appendLiteral(":").appendSecondOfMinute(2);
			return dateFormatter.toFormatter().print(new Instant(millis));
		} catch (final RuntimeException rtex) {
			return "2014 01/01 00:00:00";
		}
	}

	protected String generateDurationString(final long millis, final boolean withSeconds) {
		long timeToConvert = millis;
		final StringBuffer buffer = new StringBuffer();
		if (timeToConvert > NeoComRender.ONEDAY) {
			final double day = Math.floor(timeToConvert / NeoComRender.ONEDAY);
			buffer.append(Double.valueOf(day).intValue()).append("D ");
			timeToConvert -= day * NeoComRender.ONEDAY;
		}
		if (timeToConvert > NeoComRender.ONEHOUR) {
			final double hour = Math.floor(timeToConvert / NeoComRender.ONEHOUR);
			buffer.append(Double.valueOf(hour).intValue()).append("H ");
			timeToConvert -= hour * NeoComRender.ONEHOUR;
		}
		if (timeToConvert > NeoComRender.ONEMINUTE) {
			final double minute = Math.floor(timeToConvert / NeoComRender.ONEMINUTE);
			buffer.append(Double.valueOf(minute).intValue()).append("M");
			timeToConvert -= minute * NeoComRender.ONEMINUTE;
		}
		if (withSeconds) {
			final double second = Math.floor(timeToConvert / 1000.0);
			buffer.append(" ").append(Double.valueOf(second).intValue()).append("S");
		}
		return buffer.toString();
	}

	protected String generateDurationString2(final long millis) {
		final DateTimeFormatterBuilder timeLeftCountdown = new DateTimeFormatterBuilder();
		if (millis > NeoComRender.ONEDAY) {
			timeLeftCountdown.appendDayOfYear(1).appendLiteral("D ");
		}
		if (millis > NeoComRender.ONEHOUR) {
			timeLeftCountdown.appendHourOfDay(2).appendLiteral("H ");
		}
		if (millis > NeoComRender.ONEMINUTE) {
			timeLeftCountdown.appendMinuteOfHour(2).appendLiteral("M ").appendSecondOfMinute(2).appendLiteral('S');
		}
		return timeLeftCountdown.toFormatter().print(new Instant(millis));
	}

	protected String generatePriceString(final double price, final boolean compress, final boolean addSuffix) {
		// Generate different formats depending on the quantity and the compress
		// flag.
		// Get rid of negative numbers.
		if (compress) {
			if (Math.abs(price) > 1200000000.0) if (addSuffix)
				return NeoComRender.priceFormatter.format(price / 1000.0 / 1000.0 / 1000.0) + " B ISK";
			else
				return NeoComRender.priceFormatter.format(price / 1000.0 / 1000.0 / 1000.0);
			if (Math.abs(price) > 12000000.0) if (addSuffix)
				return NeoComRender.priceFormatter.format(price / 1000.0 / 1000.0) + " M ISK";
			else
				return NeoComRender.priceFormatter.format(price / 1000.0 / 1000.0);
		}
		if (addSuffix)
			return NeoComRender.priceFormatter.format(price) + " ISK";
		else
			return NeoComRender.priceFormatter.format(price);
	}

	protected String generateSecurityColor(double sec, final String data) {
		final StringBuffer htmlFragmentWithColor = new StringBuffer();
		String secColor = "#F00000";
		// Get the color from the table.
		if (sec < 0.0) {
			sec = 0.0;
		}
		if (sec > 1.0) {
			sec = 1.0;
		}
		final long secAdjust = Long.valueOf(Math.round(sec * 10.0)).intValue();
		secColor = NeoComRender.securityLevels.get(Long.valueOf(secAdjust).intValue());
		htmlFragmentWithColor.append("<font color='").append(secColor).append("'>").append(data).append("</font>");
		return htmlFragmentWithColor.toString();
	}

	protected String generateTimeString(final long millis) {
		try {
			final DateTimeFormatterBuilder timeFormatter = new DateTimeFormatterBuilder();
			if (millis > NeoComRender.ONEDAY) {
				timeFormatter.appendDayOfYear(1).appendLiteral("D ");
			}
			if (millis > NeoComRender.ONEHOUR) {
				timeFormatter.appendHourOfDay(2).appendLiteral(":");
			}
			if (millis > NeoComRender.ONEMINUTE) {
				timeFormatter.appendMinuteOfHour(2).appendLiteral(":").appendSecondOfMinute(2);
			}
			return timeFormatter.toFormatter().print(new Instant(millis));
		} catch (final RuntimeException rtex) {
			return "0:00";
		}
	}

	protected void loadEveIcon(final ImageView targetIcon, final int typeID) {
		this.loadEveIcon(targetIcon, typeID, false);
	}

	/**
	 * Downloads and caches the item icon from the CCP server. The new implementation check for special cases
	 * such as locations. Stations on locations have an image that can be downloaded from the same place.
	 *
	 * @param targetIcon
	 * @param typeID
	 */
	protected void loadEveIcon(final ImageView targetIcon, final int typeID, final boolean station) {
		// Check if the layout has the icon placeholder.
		if (null != targetIcon) {
			// If the flag signals an station change the code.
			String link = NeoComAppConnector.getSingleton().getCacheConnector().getURLForItem(typeID);
			if (station) {
				link = NeoComAppConnector.getSingleton().getCacheConnector().getURLForStation(typeID);
			}
			final Drawable draw = NeoComAppConnector.getSingleton().getCacheConnector().getCacheDrawable(link, targetIcon);
			targetIcon.setImageDrawable(draw);
		}
	}

	protected Spanned regionSystemLocation(final EveLocation loc) {
		final StringBuffer htmlLocation = new StringBuffer();
		// EveLocation loc =
		// NeoComAppConnector.getSingleton().getDBConnector().searchLocationbyID(getCastedModel().getBlueprintLocationID());
		final String security = loc.getSecurity();
		String secColor = NeoComRender.securityLevels.get(security);
		if (null == secColor) {
			secColor = "#2FEFEF";
		}
		// Append the Region -> system
		htmlLocation.append(loc.getRegion()).append(AppWideConstants.FLOW_ARROW_RIGHT).append(loc.getConstellation())
				.append(AppWideConstants.FLOW_ARROW_RIGHT);
		htmlLocation.append("<font color='").append(secColor).append("'>")
		            .append(NeoComRender.securityFormatter.format(loc.getSecurityValue())).append("</font>");
		htmlLocation.append(" ").append(loc.getStation());
		return Html.fromHtml(htmlLocation.toString());
	}
@Deprecated
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@SuppressWarnings("deprecation")
	protected void setBackgroundTransparency(final int resource) {
		// Set the background form the Theme.
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			_convertView.setBackgroundDrawable(this.getContext().getResources().getDrawable(resource));
		}
	}
}

// - UNUSED CODE
// ............................................................................................
