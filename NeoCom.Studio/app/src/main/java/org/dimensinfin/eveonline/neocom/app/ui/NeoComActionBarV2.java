package org.dimensinfin.eveonline.neocom.app.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.exception.NeoComException;
import org.dimensinfin.eveonline.neocom.interfaces.IURLIconProvider;
import org.dimensinfin.eveonline.neocom.sharedcode.dataservice.AndroidGlobalDataManager;

import com.squareup.picasso.Picasso;

/**
 * @author Adam Antinoo
 */
public class NeoComActionBarV2 implements ICollaboration {
	private ImageView activityIcon = null;
	private ImageView upperIcon = null;
	private ImageView lowerIcon = null;
	private String titleTopLeft = "";
	private String titleTopRight = "";
	private String subtitle = "";

	private NeoComActionBarV2() {}

	public String getTitleTopLeft() {
		return titleTopLeft;
	}

	public String getTitleTopRight() {
		return titleTopRight;
	}

	public String getSubtitle() {
		return subtitle;
	}

	// - I C O L L A B O R A T I O N
	@Override
	public List<ICollaboration> collaborate2Model( final String variation ) {
		return new ArrayList<>();
	}

	@Override
	public int compareTo( final Object o ) {
		return 0;
	}

	protected View _convertView = null;
	protected int layoutIdentifier = -1;

	// --- D A T A   F I E L D S

	// --- U I   F I E L D S

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	//	public NeoComActionBarV2( final int layoutid ) {
	//		super();
	//		this.layoutIdentifier = layoutid;
	//		initializeViews();
	//		updateViews();
	//	}
	//
	//	// - M E T H O D - S E C T I O N ..........................................................................
	//	// --- G E T T E R S   &   S E T T E R S
	//	public NeoComActionBarV2 setTitleLeft( final String title ) {
	//		if (null != title) {
	//			this.titleTopLeft.setText(title);
	//			this.titleTopLeft.setVisibility(View.VISIBLE);
	//		}
	//		return this;
	//	}
	//
	//	public NeoComActionBarV2 setTitleRight( final String title ) {
	//		if (null != title) {
	//			this.titleTopRight.setText(title);
	//			this.titleTopRight.setVisibility(View.VISIBLE);
	//		}
	//		return this;
	//	}
	//
	//	public NeoComActionBarV2 setSubtitle( final String subtitle ) {
	//		if (null != subtitle) {
	//			this.subtitle.setText(subtitle);
	//			this.subtitle.setVisibility(View.VISIBLE);
	//		}
	//		return this;
	//	}
	//
	//	public NeoComActionBarV2 setSubtitleSpanned( Spanned subtitleSpanned ) {
	//		if (null != subtitle) {
	//			this.subtitle.setText(subtitleSpanned);
	//			this.subtitle.setVisibility(View.VISIBLE);
	//		}
	//		return this;
	//	}

	public NeoComActionBarV2 setActivityIcon( final int iconReference ) {
		activityIcon.setImageResource(iconReference);
		activityIcon.setVisibility(View.VISIBLE);
		return this;
	}

	public NeoComActionBarV2 setIconTop( final IURLIconProvider provider ) {
		try {
			final String link = provider.getIconUrlLocation();
			Picasso.get().load(link).into(upperIcon);
			upperIcon.setVisibility(View.VISIBLE);
		} catch (NeoComException e) {
			upperIcon.setImageResource(R.drawable.nopilot);
		}
		return this;
	}

	public NeoComActionBarV2 setIconBottom( final IURLIconProvider provider ) {
		try {
			final String link = provider.getIconUrlLocation();
			Picasso.get().load(link).into(lowerIcon);
			lowerIcon.setVisibility(View.VISIBLE);
		} catch (NeoComException e) {
			lowerIcon.setImageResource(R.drawable.nopilot);
		}
		return this;
	}

	//	public void initializeViews() {
	//		createView();
	//		activityIcon = (ImageView) _convertView.findViewById(R.id.activityIcon);
	//		upperIcon = (ImageView) _convertView.findViewById(R.id.upperIcon);
	//		lowerIcon = (ImageView) _convertView.findViewById(R.id.lowerIcon);
	//		titleTopLeft = (TextView) _convertView.findViewById(R.id.toolBarTitleLeft);
	//		titleTopRight = (TextView) _convertView.findViewById(R.id.toolBarTitleRight);
	//		subtitle = (TextView) _convertView.findViewById(R.id.toolBarSubTitle);
	//	}

	//	public void updateViews() {
	//		activityIcon.setVisibility(View.GONE);
	//		upperIcon.setVisibility(View.GONE);
	//		lowerIcon.setVisibility(View.GONE);
	//		titleTopRight.setVisibility(View.VISIBLE); // At least the main title should be visible.
	//		titleTopRight.setVisibility(View.GONE);
	//		subtitle.setVisibility(View.GONE);
	//	}

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

	// - B U I L D E R
	public static class Builder {
		private NeoComActionBarV2 onConstruction;

		public Builder() {
			this.onConstruction = new NeoComActionBarV2();
		}

		public Builder withTitleTopLeft( final String titleTopLeft ) {
			this.onConstruction.titleTopLeft = titleTopLeft;
			return this;
		}

		public Builder withTitleTopRight( final String titleTopRight ) {
			this.onConstruction.titleTopRight = titleTopRight;
			return this;
		}

		public Builder withSubtitle( final String subtitle ) {
			this.onConstruction.subtitle = subtitle;
			return this;
		}

		public NeoComActionBarV2 build() {
			//			Objects.requireNonNull(this.onConstruction.preferences);
			return this.onConstruction;
		}
	}
}
