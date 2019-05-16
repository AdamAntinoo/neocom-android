package org.dimensinfin.eveonline.neocom.ui;

import android.app.Activity;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.dimensinfin.eveonline.neocom.R;
//import org.dimensinfin.eveonline.neocom.core.NeoComException;
import org.dimensinfin.eveonline.neocom.sharedcode.dataservice.AndroidGlobalDataManager;
import org.dimensinfin.eveonline.neocom.exception.NeoComException;
import org.dimensinfin.eveonline.neocom.interfaces.IURLIconProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Adam Antinoo
 */
public class NeoComActionBar {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = LoggerFactory.getLogger(NeoComActionBar.class);

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

	public NeoComActionBar setSubtitleSpanned( Spanned subtitleSpanned ) {
		if (null != subtitle) {
			this.subtitle.setText(subtitleSpanned);
			this.subtitle.setVisibility(View.VISIBLE);
		}
		return this;
	}

	public NeoComActionBar setActivityIcon( final int iconReference ) {
		activityIcon.setImageResource(iconReference);
		activityIcon.setVisibility(View.VISIBLE);
		return this;
	}

	public NeoComActionBar setIconTop( final IURLIconProvider provider ) {
		try {
			final String link = provider.getIconUrlLocation();
			Picasso.get().load(link).into(upperIcon);
			upperIcon.setVisibility(View.VISIBLE);
		} catch (NeoComException e) {
			upperIcon.setImageResource(R.drawable.nopilot);
		}
		return this;
	}

	public NeoComActionBar setIconBottom( final IURLIconProvider provider ) {
		try {
			final String link = provider.getIconUrlLocation();
			Picasso.get().load(link).into(lowerIcon);
			lowerIcon.setVisibility(View.VISIBLE);
		} catch (NeoComException e) {
			lowerIcon.setImageResource(R.drawable.nopilot);
		}
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
