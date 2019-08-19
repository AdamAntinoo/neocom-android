package org.dimensinfin.eveonline.neocom.app.render;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.android.mvc.render.MVCRender;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.core.interfaces.IExpandable;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.domain.EsiLocation;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.joda.time.Instant;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

import com.squareup.picasso.Picasso;

public abstract class NeoComRenderv2 extends MVCRender {
	// - C H A R A C T E R S
	public static final String FLOW_ARROW_RIGHT = " ► ";
	public static final String FLOW_ARROW_LEFT = " ◄ ";
	public static final String FLOW_ARROW_UP = " ▲ ";
	public static final String FLOW_ARROW_DOWN = " ▼ ";
	public static final String INFINITY = " ∞ ";
	public static final String HOME = " ⌂ ";
	public static final String DELTA = " ∆ ";

	protected static final long ONEMINUTE = TimeUnit.MINUTES.toMillis(1);
	protected static final long ONEHOUR = TimeUnit.HOURS.toMillis(1);
	protected static final long ONEDAY = TimeUnit.DAYS.toMillis(1);
	protected static final DecimalFormat keyFormatter = new DecimalFormat("0000000");
	protected static final DateTimeFormatter durationFormatter = DateTimeFormat.forPattern("d HH:mm");
	protected static final DateTimeFormatter timePointFormatter = DateTimeFormat
			                                                              .forPattern("YYYY.MM.dd HH:mm");
	protected static final DateTimeFormatter jobTimeFormatter = DateTimeFormat.forPattern("D HH:MM");
	protected static final DecimalFormat priceFormatter = new DecimalFormat("###,###.00");
	protected static final DecimalFormat qtyFormatter = new DecimalFormat("###,##0");
	protected static final DecimalFormat pctFormatter = new DecimalFormat("##0.00%");
	protected static final DecimalFormat moduleIndexFormatter = new DecimalFormat("000");
	protected static final DecimalFormat queueIndexFormatter = new DecimalFormat("00");
	protected static final DecimalFormat moduleMultiplierFormatter = new DecimalFormat("x##0.0");
	protected static final DecimalFormat itemCountFormatter = new DecimalFormat("###,##0");
	protected static final DecimalFormat volumeFormatter = new DecimalFormat("###,##0.0");
	protected static final DecimalFormat securityFormatter = new DecimalFormat("0.0");
	protected static final HashMap<Integer, String> securityLevels = new HashMap<Integer, String>();

	static {
		securityLevels.put(10, "#2FEFEF");
		securityLevels.put(9, "#48F0C0");
		securityLevels.put(8, "#00EF47");
		securityLevels.put(7, "#00F000");
		securityLevels.put(6, "#8FEF2F");
		securityLevels.put(5, "#EFEF00");
		securityLevels.put(4, "#D77700");
		securityLevels.put(3, "#F06000");
		securityLevels.put(2, "#F04800");
		securityLevels.put(1, "#D73000");
		securityLevels.put(0, "#F00000");
	}

	// - F I E L D - S E C T I O N
	protected ViewGroup actuatorsBlock;
	protected ImageView separator;
	protected ImageView _rightArrow;
	protected ImageView menuIcon;

	// - C O N S T R U C T O R S
	public NeoComRenderv2( @NonNull final IAndroidController controller, @NonNull final Context context ) {
		super(controller, context);
	}

	public IAndroidController<ICollaboration> getBaseController() {
		return (IAndroidController<ICollaboration>) super.getController();
	}

	// - I R E N D E R   I N T E R F A C E
	public void initializeViews() {
		// Get the view of the rightArrow if found.
		this.actuatorsBlock = this.getView().findViewById(R.id.actuatorsBlock);
		this.separator = this.getView().findViewById(R.id.separator);
		this._rightArrow = this.getView().findViewById(R.id.rightArrow);
		this.menuIcon = this.getView().findViewById(R.id.menuIcon);
	}

	/**
	 * Check if the expandable nodes have a click active. During the click show the spinner until the action finishes
	 * with a new <code>updateContent()</code>.
	 */
	@Override
	public void updateContent() {
		// Check if the model is expandable to show or hide the arrow.
		final ICollaboration targetModel = this.getBaseController().getModel();
		if (null != this.actuatorsBlock) {
			if (targetModel instanceof IExpandable) {
				this.actuatorsBlock.setVisibility(View.VISIBLE);
				this.separator.setVisibility(View.GONE);

				this._rightArrow.setVisibility(View.VISIBLE);
				if (((IExpandable) targetModel).isExpanded()) {
					_rightArrow.setImageResource(R.drawable.arrowdown);
				} else {
					_rightArrow.setImageResource(R.drawable.arrowright);
				}
			} else {
				this.actuatorsBlock.setVisibility(View.GONE);
				this.separator.setVisibility(View.VISIBLE);

				_rightArrow.setVisibility(View.GONE);
			}
			if (null != this.menuIcon) {
				if (targetModel instanceof View.OnLongClickListener) {
					this.menuIcon.setVisibility(View.VISIBLE);
				} else this.menuIcon.setVisibility(View.INVISIBLE);
			}
		}
	}

	// - C O M P L E M E N T A R Y
	public void setAvatarIcon( final ImageView target, final int pilotIdentifier ) {
		if (null != target) {
			final String link = this.getUrl4Avatar(pilotIdentifier);
			Picasso.get().load(link).into(target);
		}
	}

	protected String getUrl4Avatar( final int pilotIdentifier ) {
		return "http://image.eveonline.com/character/" + pilotIdentifier + "_256.jpg";
	}
	protected Drawable getRaceIcon(final String raceName) {
		if (null != raceName)
			switch (raceName.toUpperCase()) {
				case "MINMATAR":
					return this.getDrawable(R.drawable.minmatar);
				case "CALDARI":
					return this.getDrawable(R.drawable.caldari);
				case "AMARR":
					return this.getDrawable(R.drawable.amarr);
				case "GALLENTE":
					return this.getDrawable(R.drawable.gallente);
				default:
					return this.getDrawable(R.drawable.defaulticonplaceholder);
			}
		else return this.getDrawable(R.drawable.defaulticonplaceholder);
	}

	protected Spanned colorFormat( final String data, final String color, final String suffix ) {
		final StringBuilder htmlFragment = new StringBuilder();
		htmlFragment.append("<font color='").append(color).append("'>");
		htmlFragment.append(data);
		if (null != suffix) {
			htmlFragment.append(suffix);
		}
		htmlFragment.append("</font>");
		return Html.fromHtml(htmlFragment.toString());
	}

	protected Spanned colorFormatLocation( final EsiLocation location ) {
		final StringBuilder htmlLocation = new StringBuilder();
		final double security = location.getSecurityValue();
		htmlLocation.append(this.generateSecurityColor(security, securityFormatter.format(security)));
		htmlLocation.append(" ").append(location.getRegion()).append("-").append(location.getName());
		return Html.fromHtml(htmlLocation.toString());
	}

	/**
	 * The price of the manufacture cost on the output is not tinted with the right color. Use red for cero or
	 * negative costs, orange for less than 10% and white for higher (or maybe green for higher). This code can
	 * be refactored to use ever the same generation code.
	 */
	protected Spanned displayManufactureCost( final double cost, final double price, final boolean compress,
	                                          final boolean suffix ) {
		final StringBuilder htmlFragmentWithColor = new StringBuilder();
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

	protected String generateDateString( final long millis ) {
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

	protected String generateDurationString( final long millis, final boolean withSeconds ) {
		long timeToConvert = millis;
		final StringBuffer buffer = new StringBuffer();
		if (timeToConvert > ONEDAY) {
			final double day = Math.floor(timeToConvert / ONEDAY);
			buffer.append(Double.valueOf(day).intValue()).append("D ");
			timeToConvert -= day * ONEDAY;
		}
		if (timeToConvert > ONEHOUR) {
			final double hour = Math.floor(timeToConvert / ONEHOUR);
			buffer.append(Double.valueOf(hour).intValue()).append("H ");
			timeToConvert -= hour * ONEHOUR;
		}
		if (timeToConvert > ONEMINUTE) {
			final double minute = Math.floor(timeToConvert / ONEMINUTE);
			buffer.append(Double.valueOf(minute).intValue()).append("M");
			timeToConvert -= minute * ONEMINUTE;
		}
		if (withSeconds) {
			final double second = Math.floor(timeToConvert / 1000.0);
			buffer.append(" ").append(Double.valueOf(second).intValue()).append("S");
		}
		return buffer.toString();
	}

	protected String generateDurationString2( final long millis ) {
		final DateTimeFormatterBuilder timeLeftCountdown = new DateTimeFormatterBuilder();
		if (millis > ONEDAY) {
			timeLeftCountdown.appendDayOfYear(1).appendLiteral("D ");
		}
		if (millis > ONEHOUR) {
			timeLeftCountdown.appendHourOfDay(2).appendLiteral("H ");
		}
		if (millis > ONEMINUTE) {
			timeLeftCountdown.appendMinuteOfHour(2).appendLiteral("M ").appendSecondOfMinute(2).appendLiteral('S');
		}
		return timeLeftCountdown.toFormatter().print(new Instant(millis));
	}

	public String generatePriceString( final double price ) {
		return this.generatePriceString(price, true, true);
	}

	protected String generatePriceString( final double price, final Boolean compress, final boolean addSuffix ) {
		// Generate different formats depending on the quantity and the compress flag.
		// Get rid of negative numbers.
		if (compress) {
			if (Math.abs(price) > 1200000000.0)
				if (addSuffix)
					return priceFormatter.format(price / 1000.0 / 1000.0 / 1000.0) + " B ISK";
				else
					return priceFormatter.format(price / 1000.0 / 1000.0 / 1000.0);
			if (Math.abs(price) > 12000000.0)
				if (addSuffix)
					return priceFormatter.format(price / 1000.0 / 1000.0) + " M ISK";
				else
					return priceFormatter.format(price / 1000.0 / 1000.0);
		}
		if (addSuffix)
			return priceFormatter.format(price) + " ISK";
		else
			return priceFormatter.format(price);
	}

	protected String generateSecurityColor( double sec, final String data ) {
		final StringBuilder htmlFragmentWithColor = new StringBuilder();
		String secColor = "#F00000";
		// Get the color from the table.
		if (sec < 0.0) {
			sec = 0.0;
		}
		if (sec > 1.0) {
			sec = 1.0;
		}
		final long secAdjust = Long.valueOf(Math.round(sec * 10.0)).intValue();
		secColor = securityLevels.get(Long.valueOf(secAdjust).intValue());
		htmlFragmentWithColor.append("<font color='").append(secColor).append("'>").append(data).append("</font>");
		return htmlFragmentWithColor.toString();
	}

	protected String generateTimeString( final long millis ) {
		try {
			final DateTimeFormatterBuilder timeFormatter = new DateTimeFormatterBuilder();
			if (millis > ONEDAY) {
				timeFormatter.appendDayOfYear(1).appendLiteral("D ");
			}
			if (millis > ONEHOUR) {
				timeFormatter.appendHourOfDay(2).appendLiteral(":");
			}
			if (millis > ONEMINUTE) {
				timeFormatter.appendMinuteOfHour(2).appendLiteral(":").appendSecondOfMinute(2);
			}
			return timeFormatter.toFormatter().print(new Instant(millis));
		} catch (final RuntimeException rtex) {
			return "0:00";
		}
	}

	protected void loadEveIcon( final ImageView targetIcon, final int typeId ) {
		this.loadEveIcon(targetIcon, typeId, false);
	}

	/**
	 * Downloads and caches the item icon from the CCP server. The new implementation check for special cases
	 * such as locations. Stations on locations have an image that can be downloaded from the same place.
	 */
	protected void loadEveIcon( final ImageView targetIcon, final int typeId, final boolean station ) {
		// Check if the layout has the icon placeholder.
		if (null != targetIcon) {
			// If the flag signals an station change the code.
			String link = this.getURLForItem(typeId);
			if (station) {
				link = this.getURLForStation(typeId);
			}
			Picasso.get().load(link).into(targetIcon);
		}
	}

	protected int getColor( final int colorReference ) {
		return this.getContext().getResources().getColor(colorReference);
	}

	protected String getURLForItem( final int typeID ) {
		final String iconUrl = "http://image.eveonline.com/Type/" + typeID + "_64.png";
		return iconUrl;
	}

	protected String getURLForStation( final int typeID ) {
		final String iconUrl = "http://image.eveonline.com/Render/" + typeID + "_64.png";
		return iconUrl;
	}

	protected Spanned regionSystemLocation( final EsiLocation loc ) {
		final StringBuilder htmlLocation = new StringBuilder();
		final String security = loc.getSecurity();
		String secColor = securityLevels.get(security);
		if (null == secColor) {
			secColor = "#2FEFEF";
		}
		// Append the Region -> system
		htmlLocation.append(loc.getRegion()).append(FLOW_ARROW_RIGHT).append(loc.getConstellation())
				.append(FLOW_ARROW_RIGHT);
		htmlLocation.append("<font color='").append(secColor).append("'>")
				.append(securityFormatter.format(loc.getSecurityValue())).append("</font>");
		htmlLocation.append(" ").append(loc.getStation());
		return Html.fromHtml(htmlLocation.toString());
	}

	protected String format2hms( final long elapsed ) {
		return DurationFormatUtils.formatDuration(elapsed * 1000, "HH:mm:ss");
	}
}
