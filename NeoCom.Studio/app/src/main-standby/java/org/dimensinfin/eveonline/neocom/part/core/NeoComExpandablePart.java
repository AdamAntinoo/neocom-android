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
package org.dimensinfin.eveonline.neocom.part.core;

import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;

import org.dimensinfin.android.mvc.constants.SystemWideConstants;
import org.dimensinfin.android.mvc.core.AbstractExpandablePart;
import org.dimensinfin.android.mvc.core.OneShotTask;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.core.util.Chrono;

import java.util.concurrent.TimeUnit;

// - CLASS IMPLEMENTATION ...................................................................................
public abstract class NeoComExpandablePart extends NeoComAbstractPart implements OnClickListener {
	public enum ColorFlavour {
		WHITE, RED, ORANGE, YELLOW, GREEN, BLUE
	}
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................
	private ColorFlavour _flavour = ColorFlavour.WHITE;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public NeoComExpandablePart (final ICollaboration node) {
		super(node);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
//	public List<IAndroidPart> collaborate2View () {
//		// Update the color depending on the configured flavour.
//		switch (getFlavour()) {
//			case WHITE:
//				separatorColor = Separator.ESeparatorType.LINE_WHITE;
//				break;
//			case RED:
//				separatorColor = Separator.ESeparatorType.LINE_RED;
//				break;
//			case ORANGE:
//				separatorColor = Separator.ESeparatorType.LINE_ORANGE;
//				break;
//			case YELLOW:
//				separatorColor = Separator.ESeparatorType.LINE_YELLOW;
//				break;
//			case BLUE:
//				separatorColor = Separator.ESeparatorType.LINE_BLUE;
//				break;
//			default:
//				separatorColor = Separator.ESeparatorType.LINE_RED;
//				break;
//		}
//		return super.collaborate2View();
//	}

	public void onClick (final View view) {
		logger.info(">> [NeoComExpandablePart.onClick]");
		// TODO - This is code already on the MVC but copied because class inheritance.
		// Signal the action may take some time and launch it on the background.
		activateClick();
		// Force the update of the view by eliminating it form the Part cache. This forces a redraw and updates the model.
		invalidate();

		new Handler().postDelayed(new OneShotTask<NeoComExpandablePart>(this) {
			public void run () {
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run () {
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
//		getTarget().fireStructureChange(SystemWideConstants.events.EVENTCONTENTS_ACTIONEXPANDCOLLAPSE.name(), this, this);
		logger.info("<< [NeoComExpandablePart.onClick]");
	}

	private NeoComExpandablePart getTarget () {
		return this;
	}

	public void setFlavour (ColorFlavour newcolor) {
		_flavour = newcolor;
	}

	public ColorFlavour getFlavour () {
		return _flavour;
	}
}
// - UNUSED CODE ............................................................................................
