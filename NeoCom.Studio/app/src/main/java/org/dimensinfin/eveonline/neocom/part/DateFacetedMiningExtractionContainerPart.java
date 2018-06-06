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

import org.dimensinfin.android.mvc.core.AbstractAndroidPart;
import org.dimensinfin.android.mvc.core.AbstractPart;
import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.core.model.Separator;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.model.DateFacetedMiningExtractionContainer;
import org.dimensinfin.eveonline.neocom.model.PanelTitle;
import org.dimensinfin.eveonline.neocom.part.core.NeoComAbstractPart;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class DateFacetedMiningExtractionContainerPart extends AbstractAndroidPart {
	  // - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = LoggerFactory.getLogger("DateFacetedMiningExtractionContainerPart");

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public DateFacetedMiningExtractionContainerPart( final DateFacetedMiningExtractionContainer node ) {
		super(node);
//		this.setThemeColor(Separator.ESeparatorType.LINE_WHITE);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public DateFacetedMiningExtractionContainer getCastedModel() {
		return (DateFacetedMiningExtractionContainer) this.getModel();
	}

	// --- I A N D R O I D P A R T   I N T E R F A C E
	@Override
	public long getModelId() {
		return getCastedModel().getContainerTitle().hashCode();
	}

	@Override
	public AbstractRender selectRenderer() {
		return new DateFacetedMiningExtractionContainerRender(this, getActivity());
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("PanelTitlePart [ ");
		buffer.append("title: ").append(getCastedModel().getContainerTitle());
		buffer.append("]");
//		buffer.append("->").append(super.toString());
		return buffer.toString();
	}

	// - CLASS IMPLEMENTATION ...................................................................................
	public static class DateFacetedMiningExtractionContainerRender extends AbstractRender {
		// - F I E L D - S E C T I O N ............................................................................
		// - U I   F I E L D S
		protected TextView title = null;

		// - C O N S T R U C T O R - S E C T I O N ................................................................
		public DateFacetedMiningExtractionContainerRender( final AbstractPart newPart, final Activity context ) {
			super(newPart, context);
		}

		// - M E T H O D - S E C T I O N ..........................................................................
		@Override
		public DateFacetedMiningExtractionContainerPart getPart() {
			return (DateFacetedMiningExtractionContainerPart) super.getPart();
		}

		// --- I R E N D E R   I N T E R F A C E
		@Override
		public void initializeViews() {
			//			super.initializeViews();
			this.title = (TextView) this._convertView.findViewById(R.id.title);
		}

		@Override
		public void updateContent() {
			//			super.updateContent();
			this.title.setText(getPart().getCastedModel().getContainerTitle());
		}

		@Override
		public int accessLayoutReference() {
			return R.layout.datefaceted4list;
		}
	}
	//...........................................................................................................
}
// - UNUSED CODE ............................................................................................
//[01]
