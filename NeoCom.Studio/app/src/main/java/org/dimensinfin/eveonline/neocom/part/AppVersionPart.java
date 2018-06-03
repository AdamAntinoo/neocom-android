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
package org.dimensinfin.eveonline.neocom.part;

import android.app.Activity;
import android.widget.TextView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.dimensinfin.android.mvc.core.AbstractPart;
import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.core.model.Separator;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.datamngmt.AndroidGlobalDataManager;
import org.dimensinfin.eveonline.neocom.model.AppVersion;
import org.dimensinfin.eveonline.neocom.part.core.NeoComAbstractPart;
import org.dimensinfin.eveonline.neocom.render.TransferRender;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class AppVersionPart extends NeoComAbstractPart {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = LoggerFactory.getLogger("AppVersionPart");

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public AppVersionPart( final AppVersion node ) {
		super(node);
		this.setThemeColor(Separator.ESeparatorType.LINE_WHITE);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public AppVersion getCastedModel () {
		return (AppVersion) this.getModel();
	}

	// --- I A N D R O I D P A R T   I N T E R F A C E
	@Override
	public long getModelId() {
		return Long.valueOf(AndroidGlobalDataManager.getResourceString(R.string.appversioncode)).longValue();
	}

	@Override
	public AbstractRender selectRenderer() {
		return new AppVersionRender(this,getActivity());
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("AppVersionPart [ ");
		buffer.append("name: ").append(0);
		buffer.append("]");
		buffer.append("->").append(super.toString());
		return buffer.toString();
	}

	// - CLASS IMPLEMENTATION ...................................................................................
	public static class AppVersionRender extends TransferRender {
		// - F I E L D - S E C T I O N ............................................................................
		// --- U I   F I E L D S
		protected TextView applicationName = null;
		protected TextView applicationVersion = null;

		// - C O N S T R U C T O R - S E C T I O N ................................................................
		public AppVersionRender( final AbstractPart newPart, final Activity context ) {
			super(newPart, context);
		}

		// - M E T H O D - S E C T I O N ..........................................................................
		@Override
		public AppVersionPart getPart () {
			return (AppVersionPart) super.getPart();
		}
		@Override
		public void initializeViews () {
			super.initializeViews();
			this.applicationName = (TextView) this._convertView.findViewById(R.id.applicationName);
			this.applicationVersion = (TextView) this._convertView.findViewById(R.id.applicationVersion);
		}
		@Override
		public void updateContent () {
			super.updateContent();
			this.applicationName.setText(getPart().getCastedModel().getName());
			this.applicationVersion.setText(getPart().getCastedModel().getVersion());
		}
		@Override
		public int accessLayoutReference(){
			return R.layout.appversion4dashboard;
		}
//		@Override
//		protected void createView() {
//			_convertView = inflateView();
//			_convertView.setTag(this);
//		}
	}
	//...........................................................................................................
}
// - UNUSED CODE ............................................................................................
//[01]
