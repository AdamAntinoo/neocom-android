//	PROJECT:        NeoCom.Android (NEOC.A)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2017 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API16.
//	DESCRIPTION:		Application to get access to CCP api information and help manage industrial activities
//									for characters and corporations at Eve Online. The set is composed of some projects
//									with implementation for Android and for an AngularJS web interface based on REST
//									services on Sprint Boot Cloud.
package org.dimensinfin.eveonline.neocom.render;

import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.core.EveAbstractHolder;
import org.dimensinfin.eveonline.neocom.part.NeoComAbstractPart;
import org.dimensinfin.eveonline.neocom.part.PlanetaryManagerPart;

import android.app.Activity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

// - CLASS IMPLEMENTATION ...................................................................................
public class PlanetaryManagerRender extends EveAbstractHolder {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................

	// - L A Y O U T   F I E L D S
	private ImageView	nodeIcon		= null;
	private TextView	name				= null;
	private TextView	assetsCount	= null;
	private TextView	regionCount	= null;

	// - L A Y O U T   L A B E L S

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public PlanetaryManagerRender(final NeoComAbstractPart target, final Activity context) {
		super(target, context);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	@Override
	public PlanetaryManagerPart getPart() {
		return (PlanetaryManagerPart) super.getPart();
	}

	@Override
	public void initializeViews() {
		super.initializeViews();
		nodeIcon = (ImageView) _convertView.findViewById(R.id.nodeIcon);
		name = (TextView) _convertView.findViewById(R.id.name);
		assetsCount = (TextView) _convertView.findViewById(R.id.managerAssetsCount);
		regionCount = (TextView) _convertView.findViewById(R.id.managerRegionCount);
	}

	@Override
	public void updateContent() {
		super.updateContent();
		name.setText(this.getPart().getName());
		assetsCount.setText(this.getPart().getTAssetsCount());
		regionCount.setText(this.getPart().getTRegionCount());
		// Needs the check to test of the manager is active to select the right icon.
		if (this.getPart().checkActivation()) {
			nodeIcon.setImageDrawable(this.getContext().getResources().getDrawable(this.getPart().getActiveIcon()));
		} else {
			nodeIcon.setImageDrawable(this.getContext().getResources().getDrawable(this.getPart().getDimmedIcon()));
		}
		//		_convertView.setBackgroundResource(R.drawable.topwhiteline);
	}

	@Override
	protected void createView() {
		final LayoutInflater mInflater = (LayoutInflater) this.getContext()
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		// The view is a new view. We have to fill all the items
		_convertView = mInflater.inflate(R.layout.planetarymanager4pilot, null);
		_convertView.setTag(this);
	}
}

// - UNUSED CODE ............................................................................................
