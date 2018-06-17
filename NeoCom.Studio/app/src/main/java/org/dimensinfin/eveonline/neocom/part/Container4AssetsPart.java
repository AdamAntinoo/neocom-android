//  PROJECT:     NeoCom.Android (NEOC.A)
//  AUTHORS:     Adam Antinoo - adamantinoo.git@gmail.com
//  COPYRIGHT:   (c) 2013-2018 by Dimensinfin Industries, all rights reserved.
//  ENVIRONMENT: Android API16.
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

import com.squareup.picasso.Picasso;

import org.dimensinfin.android.mvc.core.ANodeBase4ExpandRender;
import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.android.mvc.interfaces.IPart;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.datamngmt.AndroidGlobalDataManager;
import org.dimensinfin.eveonline.neocom.model.ContainerFacetedAssetsContainer;
import org.dimensinfin.eveonline.neocom.part.core.NeoComAbstractExpandablePart;
import org.dimensinfin.eveonline.neocom.part.core.NeoComAbstractPart;
import org.dimensinfin.eveonline.neocom.render.AAssetIdentifierRender;

import java.util.Collections;
import java.util.Vector;

// - CLASS IMPLEMENTATION ...................................................................................
public class Container4AssetsPart extends AssetPart {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static final long serialVersionUID = -2462731579059844711L;

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public Container4AssetsPart( final ContainerFacetedAssetsContainer node ) {
		super(node);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public ContainerFacetedAssetsContainer getCastedModel() {
		return (ContainerFacetedAssetsContainer) this.getModel();
	}

	public String getTContentsCount() {
		return NeoComAbstractPart.qtyFormatter.format(this.getCastedModel().getContentSize());
	}

	public String getTContentsValue() {
		return NeoComAbstractPart.generatePriceString(getCastedModel().getTotalValue(), true, true);
	}

	public String getTContentsVolume() {
		return NeoComAbstractPart.qtyFormatter.format(getCastedModel().getTotalVolume()) + " m3";
	}

	//	public long getModelId() {
//		return getCastedModel().getAssetId();
//	}
//
//	/**
//	 * Container may be user named so check if the user label is defined and then return that value.
//	 */
//	public String getTAssetName() {
//		String userName = this.getCastedModel().getUserLabel();
//		if (null == userName)
//			return "#" + this.getCastedModel().getAssetId();
//		else
//			return userName;
//	}
//
//	public String getTContentCount() {
//		return NeoComAbstractPart.qtyFormatter.format(this.getCastedModel().getContentSize());
//	}
//
//	public String getTContainerCategory() {
//		return this.getCastedModel().getItem().getName();
//	}
//
//	public int getTypeID() {
//		return this.getCastedModel().getTypeId();
//	}

	/**
	 * The default actions inside this method usually are the sorting of the children nodes. Sort the container
	 * contents by name.
	 */
	public Vector<IPart> runPolicies( final Vector<IPart> targets ) {
		// Order the contents by alphabetical name.
		Collections.sort(targets, Location4AssetsPart.assetNameComparator);
		return targets;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("Container4AssetsPart [");
		buffer.append(this.getTAssetName());
		buffer.append(" ]");
		return buffer.toString();
	}

	@Override
	public AbstractRender selectRenderer() {
		return new ContainerRender(this, _activity);
	}

	// - CLASS IMPLEMENTATION ...................................................................................
	private static class ContainerRender extends AAssetIdentifierRender {
		// - S T A T I C - S E C T I O N ..........................................................................

		// - F I E L D - S E C T I O N ............................................................................
		public TextView contentValue = null;
		public TextView contentVolume = null;
		public TextView nodeClass = null;
		public TextView count = null;

		// - C O N S T R U C T O R - S E C T I O N ................................................................
		public ContainerRender( final Container4AssetsPart target, final Activity context ) {
			super(target, context);
		}

		// - M E T H O D - S E C T I O N ..........................................................................
		public Container4AssetsPart getPart() {
			return (Container4AssetsPart) super.getPart();
		}

		@Override
		public void initializeViews() {
			super.initializeViews();
			contentValue = (TextView) _convertView.findViewById(R.id.contentValue);
			contentVolume = (TextView) _convertView.findViewById(R.id.contentVolume);
			nodeClass = (TextView) _convertView.findViewById(R.id.nodeClass);
			count = (TextView) _convertView.findViewById(R.id.count);
		}

		@Override
		public void updateContent() {
			super.updateContent();
			String link = AndroidGlobalDataManager.getURLForItem(this.getPart().getCastedModel().getTypeId());
			Picasso.get().load(link).into(nodeIcon);

			contentValue.setText(this.getPart().getTContentsValue());
			contentValue.setText(this.getPart().getTContentsVolume());
			nodeClass.setText("CONTAINER");
			count.setText(this.getPart().getTContentsCount());
		}

		@Override
		public int accessLayoutReference() {
			return R.layout.container4listbylocation;
		}
	}
}

// - UNUSED CODE ............................................................................................
