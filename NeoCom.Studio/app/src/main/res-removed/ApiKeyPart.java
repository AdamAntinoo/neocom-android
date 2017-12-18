//	PROJECT:        NeoCom.Android (NEOC.A)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2017 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API16.
//	DESCRIPTION:		Application to get access to CCP api information and help manage industrial activities
//									for characters and corporations at Eve Online. The set is composed of some projects
//									with implementation for Android and for an AngularJS web interface based on REST
//									services on Sprint Boot Cloud.
package org.dimensinfin.eveonline.neocom.part;

import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.android.mvc.interfaces.INamedPart;
import org.dimensinfin.android.mvc.interfaces.IPart;
import org.dimensinfin.eveonline.neocom.constant.AppWideConstants;
import org.dimensinfin.eveonline.neocom.core.NeoComAbstractPart;
import org.dimensinfin.eveonline.neocom.core.PartComparatorFactory;
import org.dimensinfin.eveonline.neocom.model.NeoComApiKey;
import org.dimensinfin.eveonline.neocom.render.ApiKeyRender;
import org.joda.time.Instant;

import java.util.Collections;
import java.util.Vector;
import java.util.logging.Logger;

// - CLASS IMPLEMENTATION ...................................................................................
public class ApiKeyPart extends NeoComAbstractPart implements INamedPart/*, OnClickListener */{
	// - S T A T I C - S E C T I O N ..........................................................................
	private static final long	serialVersionUID	= -7718648590261849585L;
	private static Logger			logger						= Logger.getLogger("APIKeyPart");

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public ApiKeyPart(final NeoComApiKey node) {
		super(node);
//		this.getCastedModel().setExpanded(true);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public NeoComApiKey getCastedModel() {
		return (NeoComApiKey) this.getModel();
	}

	@Override
	public long getModelID() {
		return this.getCastedModel().getKey();
	}

	/**
	 * The name of an api part is the key number so I can sort the keys by number.
	 */
	public String getName() {
		return this.getTransformedKey();
	}

	public String getTransformedKey() {
		return NeoComAbstractPart.keyFormatter.format(this.getCastedModel().getKey());
	}

	public String getTransformedPaidUntil() {
		return NeoComAbstractPart.timePointFormatter.print(new Instant(this.getCastedModel().getPaidUntil()));
	}

//	/**
//	 * Manage a click on the key visible element. A click will toggle the expand/collapse state.
//	 */
//	public void onClick(final View view) {
//		// Toggle location to show its contents.
//		this.getCastedModel().toggleExpanded();
//		this.fireStructureChange(SystemWideConstants.events.EVENTSTRUCTURE_ACTIONEXPANDCOLLAPSE.name(), this, this);
//	}

	/**
	 * The default actions inside this method usually are the sorting of the children nodes. Sort the Api Keys
	 * by Name whatever that means for a key.
	 */
	@Override
	public Vector<IPart> runPolicies(final Vector<IPart> targets) {
		// Order the characters by alphabetical name.
		Collections.sort(targets, PartComparatorFactory.createPartComparator(AppWideConstants.comparators.COMPARATOR_NAME));
		return targets;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("APIKeyPart [");
		buffer.append(this.getCastedModel());
		buffer.append(" ]");
		return buffer.toString();
	}

	@Override
	protected AbstractRender selectRenderer() {
		return new ApiKeyRender(this, _activity);
	}
}

// - UNUSED CODE ............................................................................................
