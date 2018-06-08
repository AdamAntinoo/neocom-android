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

import org.dimensinfin.android.mvc.core.AbstractPart;
import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.model.RefiningDetailReport;
import org.dimensinfin.eveonline.neocom.part.core.NeoComAbstractPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class RefiningDetailPart extends NeoComAbstractPart {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = LoggerFactory.getLogger("RefiningDetailPart");

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public RefiningDetailPart( final RefiningDetailReport node ) {
		super(node);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public RefiningDetailReport getCastedModel() {
		return (RefiningDetailReport) this.getModel();
	}

	// --- M O D E L   D A T A   T R A N S F O R M A T I O N
	public String getTMarketLocation() {
		return "0.9 The Forge - Jita";
	}

	// --- I A N D R O I D P A R T   I N T E R F A C E
	@Override
	public long getModelId() {
		return getCastedModel().getTypeId();
	}

	@Override
	public AbstractRender selectRenderer() {
		return new RefiningDetailRender(this, getActivity());
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("RefiningDetailPart [ ");
		buffer.append("name: ").append(0);
		buffer.append("]");
		buffer.append("->").append(super.toString());
		return buffer.toString();
	}

	// - CLASS IMPLEMENTATION ...................................................................................
	public static class RefiningDetailRender extends AbstractRender {
		// - F I E L D - S E C T I O N ............................................................................
		private TextView resourceMarketSystem = null;

		// - C O N S T R U C T O R - S E C T I O N ................................................................
		public RefiningDetailRender( AbstractPart newPart, Activity context ) {
			super(newPart, context);
		}

		// - M E T H O D - S E C T I O N ..........................................................................
		@Override
		public RefiningDetailPart getPart() {
			return (RefiningDetailPart) super.getPart();
		}

		// --- A B S T R A C T R E N D E R   O V E R R I D E S
		@Override
		public void initializeViews() {
			resourceMarketSystem = (TextView) _convertView.findViewById(R.id.resourceMarketSystem);
		}

		@Override
		public void updateContent() {
			resourceMarketSystem.setText(this.getPart().getTMarketLocation());

		}

		@Override
		public int accessLayoutReference() {
			return R.layout.refiningdetail4header;
		}
	}
}
// - UNUSED CODE ............................................................................................
//[01]
