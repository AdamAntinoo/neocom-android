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

import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.core.model.Separator;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.model.IconicGroup;
import org.dimensinfin.eveonline.neocom.part.core.NeoComAbstractExpandablePart;
import org.dimensinfin.eveonline.neocom.render.ANodeBaseDecoratedRender;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class IconicGroupPart extends NeoComAbstractExpandablePart {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public IconicGroupPart( final IconicGroup node ) {
		super(node);
		this.setThemeColor(Separator.ESeparatorType.LINE_WHITE);
		//		this.toggleExpanded();
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public IconicGroup getCastedModel() {
		return (IconicGroup) this.getModel();
	}

	public String getTContentCount() {
		return "-";
	}

	// --- I A N D R O I D P A R T   I N T E R F A C E
	@Override
	public long getModelId() {
		return getCastedModel().hashCode();
	}

	@Override
	public AbstractRender selectRenderer() {
		return new IconicGroupRender(this, getActivity());
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("IconicGroupPart [ ");
		buffer.append("title: ").append(getCastedModel().getLabel());
		buffer.append("]");
		//		buffer.append("->").append(super.toString());
		return buffer.toString();
	}

	// - CLASS IMPLEMENTATION ...................................................................................
	public static class IconicGroupRender extends ANodeBaseDecoratedRender {
		// - F I E L D - S E C T I O N ............................................................................
		// - U I   F I E L D S
		protected TextView title = null;
		protected TextView count = null;

		// - C O N S T R U C T O R - S E C T I O N ................................................................
		public IconicGroupRender( final IconicGroupPart newPart, final Activity context ) {
			super(newPart, context);
		}

		// - M E T H O D - S E C T I O N ..........................................................................
		@Override
		public IconicGroupPart getPart() {
			return (IconicGroupPart) super.getPart();
		}

		// --- I R E N D E R   I N T E R F A C E
		@Override
		public void initializeViews() {
			super.initializeViews();
			title = (TextView) _convertView.findViewById(R.id.title);
			count = (TextView) _convertView.findViewById(R.id.count);
		}

		@Override
		public void updateContent() {
			super.updateContent();
			this.nodeIcon.setImageResource(this.getPart().getCastedModel().getIconReference());
			this.title.setText(this.getPart().getCastedModel().getLabel());
			this.count.setText(this.getPart().getTContentCount());
		}

		@Override
		public int accessLayoutReference() {
			return R.layout.iconicgroup4list;
		}
	}
	//...........................................................................................................
}
// - UNUSED CODE ............................................................................................
//[01]
