///	PROJECT:        NeoCom.Android (NEOC.A)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2017 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API22.
//	DESCRIPTION:		Application to get access to CCP api information and help manage industrial activities
//									for characters and corporations at Eve Online. This is the Android application but shares
//                  libraries and code with other application designed for alternate platforms.
//                  The model management is shown using a generic Model View Controller that allows make the
//                  rendering of the model data similar on all the platforms used.
package org.dimensinfin.eveonline.neocom.part.core;

import android.view.View;
import android.view.View.OnClickListener;

import org.dimensinfin.android.mvc.constants.SystemWideConstants;
import org.dimensinfin.android.mvc.core.OneShotTask;
import org.dimensinfin.android.mvc.interfaces.IPart;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.core.model.Separator.ESeparatorType;
import org.dimensinfin.core.util.Chrono;
import org.dimensinfin.eveonline.neocom.datamngmt.manager.GlobalAndroidDataManager;

import java.util.List;

// - CLASS IMPLEMENTATION ...................................................................................
public abstract class NeoComExpandablePart extends NeoComAbstractPart implements OnClickListener {
	public enum ColorFlavour {
		RED, ORANGE, YELLOW, GREEN
	}
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................
	private ColorFlavour _flavour = ColorFlavour.RED;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public NeoComExpandablePart (final ICollaboration node) {
		super(node);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public List<IPart> collaborate2View () {
		// Update the color depending on the configured flavour.
		switch (getFlavour()) {
			case RED:
				separatorColor = ESeparatorType.LINE_RED;
				break;
			case ORANGE:
				separatorColor = ESeparatorType.LINE_ORANGE;
				break;
			case YELLOW:
				separatorColor = ESeparatorType.LINE_YELLOW;
				break;
			default:
				separatorColor = ESeparatorType.LINE_RED;
				break;
		}
		return super.collaborate2View();
	}

	public void onClick (final View view) {
		logger.info(">> [NeoComExpandablePart.onClick]");
		// Signal the action may take some time and launch it on the background.
		activateClick();
		// Force the update of the listview.
		invalidate();
		notifyDataSetChanged();
		// Launch the actions on a new thread.
		GlobalAndroidDataManager.submitOnClick(
				new OneShotTask<NeoComExpandablePart>(this) {
					public void run () {
						logger.info(">> [NeoComExpandablePart.onClick.OneShotTask.run]");
						Chrono chrono = new Chrono();
						getTarget().toggleExpanded();
						getTarget().invalidate();
						getTarget().fireStructureChange(SystemWideConstants.events.EVENTSTRUCTURE_ACTIONEXPANDCOLLAPSE.name(), this, this);
						completeClick();
						logger.info("<< [NeoComExpandablePart.onClick.OneShotTask.run]> Time Elapsed: " + chrono.printElapsed(Chrono.ChonoOptions.SHOWMILLIS));
					}
				});
		GlobalAndroidDataManager.submitOnClick(
				new OneShotTask<NeoComExpandablePart>(this) {
					public void run () {
						logger.info(">> [StructureWithContentsPart.onClick.OneShotTask.run]");
						getTarget().fireStructureChange(SystemWideConstants.events.EVENTSTRUCTURE_DOWNLOADDATA.name(), this, this);
						logger.info("<< [StructureWithContentsPart.onClick.OneShotTask.run]");
					}
				});
		logger.info("<< [NeoComExpandablePart.onClick]");
	}

	public void setFlavour (ColorFlavour newcolor) {
		_flavour = newcolor;
	}

	public ColorFlavour getFlavour () {
		return _flavour;
	}
}
// - UNUSED CODE ............................................................................................
