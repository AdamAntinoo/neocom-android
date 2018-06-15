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
package org.dimensinfin.eveonline.neocom.part.core;

import java.util.Hashtable;
import java.util.Hashtable;
import java.util.Hashtable;
import java.util.List;

import org.dimensinfin.android.mvc.core.AbstractExpandablePart;
import org.dimensinfin.android.mvc.interfaces.IAndroidPart;
import org.dimensinfin.android.mvc.interfaces.IPart;
import org.dimensinfin.android.mvc.part.SeparatorPart;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.core.model.Separator;
import org.joda.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public abstract class NeoComAbstractExpandablePart extends AbstractExpandablePart {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = LoggerFactory.getLogger("NeoComAbstractExpandablePart");

	// - F I E L D - S E C T I O N ............................................................................
	private Separator.ESeparatorType _themeColor = Separator.ESeparatorType.LINE_WHITE;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public NeoComAbstractExpandablePart( ICollaboration model ) {
		super(model);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	// --- P A N E L   C O L O R   T H E M E
	public Separator.ESeparatorType getThemeColor() {
		return _themeColor;
	}

	public IAndroidPart setThemeColor( final Separator.ESeparatorType themeColor ) {
		this._themeColor = themeColor;
		return this;
	}
	/**
	 * New implementation for the collaboration to the view for all NeoCom parts using a Part collector that allows to
	 * use a single final list so no more copies and memory use to keep all the changes on the flow. This is a full
	 * optimization that maybe can also be used to feed the Adapter in real time when the changes are collected on the
	 * part list.
	 * @param contentCollector
	 * @return
	 */
	@Deprecated
	public void collaborate2View( List<IAndroidPart> contentCollector ) {
		//		ArrayList<IPart> result = new ArrayList<IPart>();
		// If the node is expanded then give the children the opportunity to also be added.
		if (this.isExpanded()) {
			if (this.isEmpty()) {
				if (this.isRenderWhenEmpty()) {
					// The region is surrounded with the Red Separator at the beginning and at the end.
					contentCollector.add(new SeparatorPart(new Separator().setType(getThemeColor()))
							.setRenderMode(this.getRenderMode()));
					contentCollector.add(this);
					// This region is empty of contents. Add the Empty Separator and complete.
					contentCollector.add(new SeparatorPart(new Separator().setType(Separator.ESeparatorType.EMPTY_SIGNAL))
							.setRenderMode(this.getRenderMode()));
					contentCollector.add(new SeparatorPart(new Separator().setType(getThemeColor()))
							.setRenderMode(this.getRenderMode()));
				}
			} else {
				// Generate the list of Parts that should be collaborated to the ListView.
				// The region is surrounded with the Red Separator at the beginning and at the end.
				contentCollector.add(
						new SeparatorPart(new Separator().setType(getThemeColor())).setRenderMode(this.getRenderMode()));
				contentCollector.add(this);
				// --- This is the section where we get the Contents and then we apply the customized Part policies.
				List<IPart> childParts = this.runPolicies(this.getChildren());
				// --- End of policies
				for (IPart part : childParts) {
					if (part instanceof IAndroidPart)
						// For each of the children that are registered to this part and that have a model recursively search for collaborations.
						((IAndroidPart) part).collaborate2View(contentCollector);
					//					if ( !part.isNewImplemented() ) {
					//						result.add(part);
					//					}
					//				contentCollector.addAll(collaboration);
				}
				contentCollector.add(
						new SeparatorPart(new Separator().setType(getThemeColor())).setRenderMode(this.getRenderMode()));
			}
		} else {
			// Check for items that will not shown when empty and not expanded.
			if (this.isRenderWhenEmpty()) {
				//				runDependencies();
				contentCollector.add(this);
			}
		}
		//		return contentCollector;
	}
}
// - UNUSED CODE ............................................................................................
//[01]
