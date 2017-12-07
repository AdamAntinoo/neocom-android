//	PROJECT:        EveIndustrialAssistant (EIA)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2014 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API11.
//	DESCRIPTION:		Application helper for Eve Online Industrialists. Will help on Minery and mainly on Manufacture.

package org.dimensinfin.eveonline.neocom.part;

// - IMPORT SECTION .........................................................................................
import org.dimensinfin.android.model.AbstractViewableNode;
import org.dimensinfin.android.model.Separator;
import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.core.model.AbstractComplexNode;
import org.dimensinfin.eveonline.neocom.holder.TerminatorHolder;

import java.util.GregorianCalendar;
import java.util.logging.Logger;

// - CLASS IMPLEMENTATION ...................................................................................
public class TerminatorPart extends NeoComAbstractPart {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static final long	serialVersionUID	= -8085543451527813221L;
	private static Logger			logger						= Logger.getLogger("TerminatorPart");

	// - F I E L D - S E C T I O N ............................................................................
	//	private AbstractPilotBasedActivity	activity					= null;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public TerminatorPart(final AbstractComplexNode node) {
		super((AbstractViewableNode) node);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public Separator getCastedModel() {
		return (Separator) this.getModel();
	}

	@Override
	public long getModelID() {
		return GregorianCalendar.getInstance().getTimeInMillis();
	}

	@Override
	protected AbstractRender selectRenderer() {
		// Get the proper holder from the render mode.
		return new TerminatorHolder(this, _activity);
	}

}

// - UNUSED CODE ............................................................................................
