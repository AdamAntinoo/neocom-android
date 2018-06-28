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

import android.os.Handler;
import android.view.View;

import org.dimensinfin.android.mvc.constants.SystemWideConstants;
import org.dimensinfin.android.mvc.core.OneShotTask;
import org.dimensinfin.android.mvc.interfaces.IAndroidPart;
import org.dimensinfin.android.mvc.interfaces.IPart;
import org.dimensinfin.android.mvc.part.SeparatorPart;
import org.dimensinfin.core.model.Separator;
import org.dimensinfin.core.util.Chrono;
import org.dimensinfin.eveonline.neocom.interfaces.IShipContainer;
import org.dimensinfin.eveonline.neocom.model.NeoComNode;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public abstract class NeoComAbstractExpandablePart extends AColorThemedPart<NeoComNode> implements View.OnClickListener {
	// - S T A T I C - S E C T I O N ..........................................................................
	//	private static Logger logger = LoggerFactory.getLogger("NeoComAbstractExpandablePart");

	// - F I E L D - S E C T I O N ............................................................................
	private Separator.ESeparatorType _themeColor = Separator.ESeparatorType.LINE_WHITE;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public NeoComAbstractExpandablePart( NeoComNode model ) {
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

	public void onClick( final View view ) {
		logger.info(">> [AbstractExpandablePart.onClick]");
		// Signal the action may take some time and launch it on the background.
		activateClick();
		// Force the update of the view by eliminating it form the Part cache. This forces a redraw and updates the model.
		invalidate();

		new Handler().postDelayed(new OneShotTask<NeoComAbstractExpandablePart>(this) {
			public void run() {
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						logger.info(">> [NeoComExpandablePart.onClick.OneShotTask.run]");
						Chrono chrono = new Chrono();
						getTarget().toggleExpanded();
						getTarget().fireStructureChange(SystemWideConstants.events.EVENTCONTENTS_ACTIONEXPANDCOLLAPSE.name(), this, this);
						completeClick();
						getTarget().invalidate();
						logger.info("<< [NeoComExpandablePart.onClick.OneShotTask.run]> Time Elapsed: " + chrono.printElapsed
								(Chrono.ChronoOptions.SHOWMILLIS));
					}
				});
			}
		}, TimeUnit.MICROSECONDS.toMillis(500));
		logger.info("<< [NeoComExpandablePart.onClick]");
	}

	private NeoComAbstractExpandablePart getTarget() {
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
	public void collaborate2View( List<IAndroidPart> contentCollector ) {
		//		ArrayList<IPart> result = new ArrayList<IPart>();
		// If the node is expanded then give the children the opportunity to also be added.
		if ( this.isExpanded() ) {
			if ( this.isEmpty() ) {
				if ( this.isRenderWhenEmpty() ) {
					// The node is surrounded with a Separator at the beginning and at the end.
					contentCollector.add(new SeparatorPart(new Separator()
							.setType(getThemeColor()))
							.setRenderMode(this.getRenderMode()));
					contentCollector.add(this);
					contentCollector.add(new SeparatorPart(new Separator()
							.setType(Separator.ESeparatorType.EMPTY_SIGNAL))
							.setRenderMode(this.getRenderMode()));
					contentCollector.add(new SeparatorPart(new Separator()
							.setType(getThemeColor()))
							.setRenderMode(this.getRenderMode()));
				}
			} else {
				// Generate the list of Parts that should be collaborated to the ListView.
				// The region is surrounded with the Red Separator at the beginning and at the end.
				contentCollector.add(new SeparatorPart(new Separator()
						.setType(getThemeColor()))
						.setRenderMode(this.getRenderMode()));
				contentCollector.add(this);
				// --- This is the section where we get the Contents and then we apply the customized Part policies.
				List<IPart> childParts = this.runPolicies(this.getChildren());
				// --- End of policies
				for (IPart part : childParts) {
					if ( part instanceof IAndroidPart )
						// For each of the children that are registered to this part and that have a model recursively search for collaborations.
						((IAndroidPart) part).collaborate2View(contentCollector);
				}
				contentCollector.add(new SeparatorPart(new Separator()
						.setType(getThemeColor()))
						.setRenderMode(this.getRenderMode()));
			}
		} else {
			// Check for items that will not shown when empty and not expanded.
			if ( this.isRenderWhenEmpty() ) {
				contentCollector.add(this);
			}
			// There are items that not being expandible can contain other objects. Expand them.
			if ( this.getModel() instanceof IShipContainer ) {
				// --- This is the section where we get the Contents and then we apply the customized Part policies.
				List<IPart> childParts = this.runPolicies(this.getChildren());
				// --- End of policies
				for (IPart part : childParts) {
					if ( part instanceof IAndroidPart )
						// For each of the children that are registered to this part and that have a model recursively search for collaborations.
						((IAndroidPart) part).collaborate2View(contentCollector);
				}
			}
		}
	}
}
// - UNUSED CODE ............................................................................................
//[01]
