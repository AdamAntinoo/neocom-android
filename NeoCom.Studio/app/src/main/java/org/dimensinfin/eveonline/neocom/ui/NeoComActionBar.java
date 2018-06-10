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
package org.dimensinfin.eveonline.neocom.ui;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.datamngmt.AndroidGlobalDataManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class NeoComActionBar {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = LoggerFactory.getLogger("NeoComActionBar");

	// - F I E L D - S E C T I O N ............................................................................
	protected View _convertView = null;
	protected int layoutIdentifier = -1;

	// --- D A T A   F I E L D S

	// --- U I   F I E L D S
	private ImageView activityIcon = null;
	private ImageView upperIcon = null;
	private ImageView lowerIcon = null;
	private TextView titleTopLeft;
	private TextView titleTopRight;
	private TextView subtitle;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public NeoComActionBar( final int layoutid ) {
		super();
		this.layoutIdentifier = layoutid;
		initializeViews();
		updateViews();
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	// --- G E T T E R S   &   S E T T E R S
	public NeoComActionBar setTitleLeft( final String title ) {
		if (null != title) {
			this.titleTopLeft.setText(title);
			this.titleTopLeft.setVisibility(View.VISIBLE);
		}
		return this;
	}

	public NeoComActionBar setTitleRight( final String title ) {
		if (null != title) {
			this.titleTopRight.setText(title);
			this.titleTopRight.setVisibility(View.VISIBLE);
		}
		return this;
	}

	public NeoComActionBar setSubtitle( final String subtitle ) {
		if (null != subtitle) {
			this.subtitle.setText(subtitle);
			this.subtitle.setVisibility(View.VISIBLE);
		}
		return this;
	}

	public NeoComActionBar setActivityIcon( final int iconReference ) {
		activityIcon.setImageResource(iconReference);
		activityIcon.setVisibility(View.VISIBLE);
		return this;
	}

	public NeoComActionBar setIconTop( final int iconReference ) {
		final String link = "http://image.eveonline.com/character/" + iconReference + "_256.jpg";
		Picasso.get().load(link).into(upperIcon);
		upperIcon.setVisibility(View.VISIBLE);
		return this;
	}
	public NeoComActionBar setIconBottom( final int iconReference ) {
		final String link = "http://image.eveonline.com/character/" + iconReference + "_256.jpg";
		Picasso.get().load(link).into(lowerIcon);
		lowerIcon.setVisibility(View.VISIBLE);
		return this;
	}

	public void initializeViews() {
		createView();
		activityIcon = (ImageView) _convertView.findViewById(R.id.activityIcon);
		upperIcon = (ImageView) _convertView.findViewById(R.id.upperIcon);
		lowerIcon = (ImageView) _convertView.findViewById(R.id.lowerIcon);
		titleTopLeft = (TextView) _convertView.findViewById(R.id.toolBarTitleLeft);
		titleTopRight = (TextView) _convertView.findViewById(R.id.toolBarTitleRight);
		subtitle = (TextView) _convertView.findViewById(R.id.toolBarSubTitle);
	}

	public void updateViews() {
		activityIcon.setVisibility(View.GONE);
		upperIcon.setVisibility(View.GONE);
		lowerIcon.setVisibility(View.GONE);
		titleTopRight.setVisibility(View.VISIBLE); // At least the main title should be visible.
		titleTopRight.setVisibility(View.GONE);
		subtitle.setVisibility(View.GONE);
	}

	public View getView() {
		return _convertView;
	}

	protected int accessLayoutReference() {
		return layoutIdentifier;
	}

	private LayoutInflater getInflater() {
		return (LayoutInflater) AndroidGlobalDataManager.getAppContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	}

	protected View inflateView( int layoutIdentifier ) {
		return getInflater().inflate(layoutIdentifier, null);
	}

	protected void createView() {
		_convertView = inflateView(accessLayoutReference());
		_convertView.setTag(this);
	}

	//	@Override
	//	public String toString() {
	//		StringBuffer buffer = new StringBuffer("NeoComActionBar [ ");
	//		buffer.append("name: ").append(0);
	//		buffer.append("]");
	//		buffer.append("->").append(super.toString());
	//		return buffer.toString();
	//	}
}
// - UNUSED CODE ............................................................................................
//[01]
