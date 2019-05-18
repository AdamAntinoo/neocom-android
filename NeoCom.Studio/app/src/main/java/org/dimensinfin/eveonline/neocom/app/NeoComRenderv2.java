package org.dimensinfin.eveonline.neocom.app;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageView;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.android.mvc.render.AMVCRender;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.core.interfaces.IExpandable;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.sharedcode.dataservice.AndroidGlobalDataManager;
import org.dimensinfin.eveonline.neocom.model.EveLocation;

import androidx.annotation.NonNull;
import com.squareup.picasso.Picasso;
import org.joda.time.Instant;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

public abstract class NeoComRenderv2 extends AMVCRender {
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

	//	public static Typeface daysFace = Typeface
	//			.createFromAsset(GlobalDataManagerStorage.getAssets(), "fonts/Days.otf");

	// - F I E L D - S E C T I O N
	protected ImageView _rightArrow = null;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public NeoComRenderv2( @NonNull final IAndroidController controller, @NonNull final Context context ) {
		super(controller, context);
	}

	public IAndroidController<ICollaboration> getBaseController() {
		return (IAndroidController<ICollaboration>) super.getController();
	}

	// - I R E N D E R   I N T E R F A C E
	public void initializeViews() {
		// Get the view of the rightArrow if found.
		_rightArrow = this.getView().findViewById(R.id.rightArrow);
	}

	/**
	 * Check if the expandable nodes have a click active. During the click show the spinner until the action finishes
	 * with a new <code>updateContent()</code>.
	 */
	@Override
	public void updateContent() {
		// Check if the model is expandable to show or hide the arrow.
		final ICollaboration targetModel = this.getBaseController().getModel();
		if (null != _rightArrow) {
			if (targetModel instanceof IExpandable) {
				_rightArrow.setVisibility(View.VISIBLE);
				if (((IExpandable) targetModel).isExpanded()) {
					_rightArrow.setImageResource(R.drawable.arrowdown);
				} else {
					_rightArrow.setImageResource(R.drawable.arrowright);
				}

				// Check if the click is running and then replace the right arrow by an spinner.
				// TODO - Activate back the click running for the NewCom expandable controllers
				//				if (getController().clickRunning()) {
				//					if (null != _rightArrow) {
				//						logger.info("-- [NeoComRender.updateContent]> Activating spinner animation.");
				//						_rightArrow.setImageDrawable(getDrawable(R.drawable.progress_spinner_orange));
				//						Animation rotation = AnimationUtils.loadAnimation(getContext(), R.anim.clockwise_rotation);
				//						rotation.setRepeatCount(Animation.INFINITE);
				//						_rightArrow.startAnimation(rotation);
				//					}
				//				}
			} else _rightArrow.setVisibility(View.GONE);
		}
	}

	protected Spanned colorFormat( final String data, final String color, final String suffix ) {
		final StringBuffer htmlFragment = new StringBuffer();
		htmlFragment.append("<font color='").append(color).append("'>");
		htmlFragment.append(data);
		if (null != suffix) {
			htmlFragment.append(suffix);
		}
		htmlFragment.append("</font>");
		return Html.fromHtml(htmlFragment.toString());
	}

	protected Spanned colorFormatLocation( final EveLocation location ) {
		final StringBuffer htmlLocation = new StringBuffer();
		final double security = location.getSecurityValue();
		htmlLocation.append(this.generateSecurityColor(security, NeoComRender.securityFormatter.format(security)));
		htmlLocation.append(" ").append(location.getRegion()).append("-").append(location.getName());
		return Html.fromHtml(htmlLocation.toString());
	}

	//	@Override
	//	abstract protected void createView ();

	/**
	 * The price of the manufacture cost on the output is not tinted with the right color. Use red for cero or
	 * negative costs, orange for less than 10% and white for higher (or maybe green for higher). This code can
	 * be refactored to use ever the same generation code.
	 */
	protected Spanned displayManufactureCost( final double cost, final double price, final boolean compress,
	                                          final boolean suffix ) {
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

	protected String generateDurationString2( final long millis ) {
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

	protected String generatePriceString( final double price ) {
		return this.generatePriceString(price, true, true);
	}

	protected String generatePriceString( final double price, final Boolean compress, final boolean addSuffix ) {
		// Generate different formats depending on the quantity and the compress flag.
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

	protected String generateSecurityColor( double sec, final String data ) {
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

	protected String generateTimeString( final long millis ) {
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
			String link = AndroidGlobalDataManager.getURLForItem(typeId);
			if (station) {
				link = AndroidGlobalDataManager.getURLForStation(typeId);
			}
			Picasso.get().load(link).into(targetIcon);
			//			try {
			//			final Future<Drawable> draw = AndroidGlobalDataManager.getCacheDrawable(link, targetIcon, getContext());
			//				targetIcon.setImageDrawable(draw.get());
			//			} catch (InterruptedException e) {
			//				e.printStackTrace();
			//			} catch (ExecutionException e) {
			//				e.printStackTrace();
			//			}
		}
	}

	protected Spanned regionSystemLocation( final EveLocation loc ) {
		final StringBuffer htmlLocation = new StringBuffer();
		// EveLocation loc =
		// NeoComAppConnector.getSingleton().getDBConnector().searchLocationbyID(getCastedModel().getBlueprintLocationID());
		final String security = loc.getSecurity();
		String secColor = NeoComRender.securityLevels.get(security);
		if (null == secColor) {
			secColor = "#2FEFEF";
		}
		// Append the Region -> system
		htmlLocation.append(loc.getRegion()).append(FLOW_ARROW_RIGHT).append(loc.getConstellation())
				.append(FLOW_ARROW_RIGHT);
		htmlLocation.append("<font color='").append(secColor).append("'>")
				.append(NeoComRender.securityFormatter.format(loc.getSecurityValue())).append("</font>");
		htmlLocation.append(" ").append(loc.getStation());
		return Html.fromHtml(htmlLocation.toString());
	}
}
