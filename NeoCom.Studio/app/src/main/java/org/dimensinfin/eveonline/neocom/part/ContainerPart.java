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
import android.widget.ImageView;
import android.widget.TextView;

import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.android.mvc.interfaces.IPart;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.model.SpaceContainer;
import org.dimensinfin.eveonline.neocom.part.core.NeoComExpandablePart;
import org.dimensinfin.eveonline.neocom.render.NewBaseExpandableRender;

import java.util.Collections;
import java.util.Vector;

// - CLASS IMPLEMENTATION ...................................................................................
public class ContainerPart extends NeoComExpandablePart /*implements OnClickListener*/ {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static final long serialVersionUID = -2462731579059844711L;

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public ContainerPart (final SpaceContainer node) {
		super(node);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public SpaceContainer getCastedModel () {
		return (SpaceContainer) this.getModel();
	}
	public long getModelID () {
		return getCastedModel().getAssetId();
	}
	/**
	 * Container may be user named so check if the user label is defined and then return that value.
	 */
	public String getTAssetName () {
		String userName = this.getCastedModel().getUserLabel();
		if ( null == userName )
			return "#" + this.getCastedModel().getAssetId();
		else
			return userName;
	}

	public String getTContentCount () {
		return qtyFormatter.format(this.getCastedModel().getContentsSize());
	}
		public String getTContainerCategory() {
			return this.getCastedModel().getItem().getName();
		}

	public int getTypeID () {
		return this.getCastedModel().getTypeId();
	}
	/**
	 * The default actions inside this method usually are the sorting of the children nodes. Sort the container
	 * contents by name.
	 */
	public Vector<IPart> runPolicies (final Vector<IPart> targets) {
		// Order the contents by alphabetical name.
		Collections.sort(targets, AssetPart.AssetNameComparator);
		return targets;
	}

	@Override
	public String toString () {
		StringBuffer buffer = new StringBuffer("ContainerPart [");
		buffer.append(this.getTAssetName());
		buffer.append(" ]");
		return buffer.toString();
	}

	@Override
	protected AbstractRender selectRenderer () {
		return new ContainerRender(this, _activity);
	}

	// - CLASS IMPLEMENTATION ...................................................................................
	private static class ContainerRender extends NewBaseExpandableRender {
		// - S T A T I C - S E C T I O N ..........................................................................

		// - F I E L D - S E C T I O N ............................................................................
		public TextView assetName = null;
		public TextView nodeClass = null;
		public TextView containerCategory = null;
		public TextView count = null;

		// - C O N S T R U C T O R - S E C T I O N ................................................................
		public ContainerRender (final ContainerPart target, final Activity context) {
			super(target, context);
		}

		// - M E T H O D - S E C T I O N ..........................................................................
		@Override
		public ContainerPart getPart () {
			return (ContainerPart) super.getPart();
		}

		@Override
		public void initializeViews () {
			super.initializeViews();
			assetName = (TextView) _convertView.findViewById(R.id.assetName);
			nodeClass = (TextView) _convertView.findViewById(R.id.nodeClass);
			containerCategory = (TextView) _convertView.findViewById(R.id.containerCategory);
			count = (TextView) _convertView.findViewById(R.id.count);
		}

		public void updateContent () {
			super.updateContent();
			assetName.setText(getPart().getTAssetName());
			nodeClass.setText("CONTAINER");
			containerCategory.setText(getPart().getTContainerCategory());
			count.setText(getPart().getTContentCount());
			loadEveIcon((ImageView) _convertView.findViewById(R.id.nodeIcon), getPart().getTypeID());
		}

		@Override
		protected void createView () {
			_convertView = inflateView(R.layout.container4listbylocation);
			_convertView.setTag(this);
		}
	}
}

// - UNUSED CODE ............................................................................................
