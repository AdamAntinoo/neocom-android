///	PROJECT:        NeoCom.Android (NEOC.A)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2017 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API22.
//	DESCRIPTION:		Application to get access to CCP api information and help manage industrial activities
//									for characters and corporations at Eve Online. This is the Android application but shares
//                  libraries and code with other application designed for alternate platforms.
//                  The model management is shown using a generic Model View Controller that allows make the
//                  rendering of the model data similar on all the platforms used.
package org.dimensinfin.eveonline.neocom.part;

import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;

import org.dimensinfin.android.mvc.constants.SystemWideConstants;
import org.dimensinfin.android.mvc.core.OneShotTask;
import org.dimensinfin.core.constant.CoreConstants;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.core.util.Chrono;

// - CLASS IMPLEMENTATION ...................................................................................
public abstract class NeoComExpandablePart extends NeoComAbstractPart implements OnClickListener {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public NeoComExpandablePart (final ICollaboration node) {
		super(node);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public void onClick (final View view) {
		logger.info(">> [NeoComExpandablePart.onClick]");
		// Signal the action may take some time and launch it on the background.
		activateClick();
		// Force the update of the listview.
		invalidate();
		notifyDataSetChanged();
		// Launch the actions on a new thread.
		new Handler().postDelayed(new OneShotTask<NeoComExpandablePart>(this) {
			public void run () {
				logger.info(">> [NeoComExpandablePart.onClick.OneShotTask.run]");
				Chrono chrono = new Chrono();
				getTargetPart().toggleExpanded();
				getTargetPart().invalidate();
				getTargetPart().fireStructureChange(SystemWideConstants.events.EVENTSTRUCTURE_ACTIONEXPANDCOLLAPSE.name(), this, this);
				completeClick();
				logger.info("<< [NeoComExpandablePart.onClick.OneShotTask.run]> Time Elapsed: " + chrono.printElapsed(Chrono.ChonoOptions.SHOWMILLIS));
			}
		}, CoreConstants.HUNDRETH);
		logger.info("<< [NeoComExpandablePart.onClick]");
	}
}
// - UNUSED CODE ............................................................................................
