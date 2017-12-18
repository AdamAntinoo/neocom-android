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

import android.app.Activity;

import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.android.mvc.interfaces.INamedPart;
import org.dimensinfin.android.mvc.interfaces.IPart;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.core.PartComparatorFactory;
import org.dimensinfin.eveonline.neocom.core.PartComparatorFactory.ComparatorType;
import org.dimensinfin.eveonline.neocom.model.Login;
import org.dimensinfin.eveonline.neocom.part.core.NeoComExpandablePart;
import org.dimensinfin.eveonline.neocom.render.BaseExpandableRender;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

// - CLASS IMPLEMENTATION ...................................................................................
public class LoginPart extends NeoComExpandablePart implements INamedPart/*, OnClickListener*/ {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static final long serialVersionUID = -7696249781992547826L;
	private static DecimalFormat itemCountFormatter = new DecimalFormat("###,##0");

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public LoginPart (final Login node) {
		super(node);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public Login getCastedModel () {
		return (Login) this.getModel();
	}

	public long getModelID () {
		return this.getCastedModel().getName().hashCode();
	}

	public String getName () {
		return this.getCastedModel().getName();
	}

	public String getTContentCount () {
		return LoginPart.itemCountFormatter.format(this.getCastedModel().getCharacters().size());
	}

	/**
	 * Sort the Regions by their names.
	 */
	@Override
	public List<IPart> runPolicies (final List<IPart> targets) {
		// Order the characters by alphabetical name.
		Collections.sort(targets, PartComparatorFactory.createPartComparator(ComparatorType.COMPARATOR_NAME));
		return targets;
	}

	@Override
	public String toString () {
		StringBuffer buffer = new StringBuffer("LoginPart [");
		buffer.append(this.getCastedModel().getName());
		buffer.append(" ]");
		return buffer.toString();
	}

	@Override
	protected AbstractRender selectRenderer () {
		return new LoginRender(this, _activity);
	}
}

//- CLASS IMPLEMENTATION ...................................................................................
final class LoginRender extends BaseExpandableRender {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................

	// - L A Y O U T   F I E L D S
	//	public TextView name = null;
	//	public TextView childCount = null;
	//
	//	// - L A Y O U T   L A B E L S
	//	public TextView titleLabel = null;
	//	public TextView locationLabel = null;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public LoginRender (final LoginPart target, final Activity context) {
		super(target, context);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	@Override
	public LoginPart getPart () {
		return (LoginPart) super.getPart();
	}

	//	@Override
	//	public void initializeViews () {
	//		super.initializeViews();
	//		name = (TextView) _convertView.findViewById(R.id.name);
	//		childCount = (TextView) _convertView.findViewById(R.id.childCount);
	//
	//		titleLabel = (TextView) _convertView.findViewById(R.id.titleLabel);
	//		locationLabel = (TextView) _convertView.findViewById(R.id.locationLabel);
	//	}

	@Override
	public void updateContent () {
		logger.info(">> [LoginRender.updateContent]");
		super.updateContent();
		nodeIcon.setImageDrawable(getContext().getResources().getDrawable(R.drawable.nopilot, getContext().getTheme()));
		name.setText(this.getPart().getName());
		childCount.setText(this.getPart().getTContentCount());

		// Set this variant labels
		classLabel.setText("LOGIN");
		categoryLabel.setText("CHARACTERS");
		_convertView.setBackground(getContext().getResources().getDrawable(R.drawable.panelborderred, getContext().getTheme()));
		logger.info("<< [LoginRender.updateContent]");
	}
}

// - UNUSED CODE ............................................................................................
