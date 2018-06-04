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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.dimensinfin.android.mvc.core.AbstractPart;
import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.part.core.NeoComAbstractPart;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class MiningExtractionPart extends NeoComAbstractPart {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = LoggerFactory.getLogger("MiningExtractionPart");

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public MiningExtractionPart( final ICollaboration model ) {
		super(model);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	@Override
	public long getModelId() {
		return 0;
	}

	@Override
	public AbstractRender selectRenderer() {
		return new MiningExtractionRender(this,this.getActivity());
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("MiningExtractionPart [ ");
		buffer.append("name: ").append(0);
		buffer.append("]");
		buffer.append("->").append(super.toString());
		return buffer.toString();
	}

	// - CLASS IMPLEMENTATION ...................................................................................
	public static class MiningExtractionRender extends AbstractRender {
		// - F I E L D - S E C T I O N ............................................................................

		// - C O N S T R U C T O R - S E C T I O N ................................................................
		public MiningExtractionRender( final AbstractPart newPart, final Activity context ) {
			super(newPart, context);
		}

		// - M E T H O D - S E C T I O N ..........................................................................
		@Override
		public void initializeViews() {

		}

		@Override
		public void updateContent() {

		}

		@Override
		public int accessLayoutReference() {
			return R.id.applicationName;
		}
	}
}
// - UNUSED CODE ............................................................................................
//[01]
