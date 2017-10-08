//	PROJECT:        NeoCom.Android (NEOC.A)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2016 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API16.
//	DESCRIPTION:		Application to get access to CCP api information and help manage industrial activities
//									for characters and corporations at Eve Online. The set is composed of some projects
//									with implementation for Android and for an AngularJS web interface based on REST
//									services on Sprint Boot Cloud.
package org.dimensinfin.eveonline.neocom.part;

// - IMPORT SECTION .........................................................................................
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Vector;

import org.dimensinfin.android.mvc.constants.SystemWideConstants;
import org.dimensinfin.android.mvc.core.AbstractAndroidPart;
import org.dimensinfin.android.mvc.core.AbstractHolder;
import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.android.mvc.interfaces.INamedPart;
import org.dimensinfin.android.mvc.interfaces.IPart;
import org.dimensinfin.core.model.AbstractComplexNode;
import org.dimensinfin.eveonline.neocom.NeoComApp;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.constant.AppWideConstants;
import org.dimensinfin.eveonline.neocom.core.EveAbstractHolder;
import org.dimensinfin.eveonline.neocom.model.Login;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

// - CLASS IMPLEMENTATION ...................................................................................
public class LoginPart extends AbstractAndroidPart implements INamedPart, OnClickListener {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static final long			serialVersionUID		= -7696249781992547826L;
	//	private static Logger					logger							= Logger.getLogger("RegionPart");
	private static DecimalFormat	itemCountFormatter	= new DecimalFormat("###,##0");

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public LoginPart(final AbstractComplexNode node) {
		super(node);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public String get_contentCount() {
		return LoginPart.itemCountFormatter.format(this.getCastedModel().getCharacters().size());
	}

	public String get_name() {
		return this.getCastedModel().getName();
	}

	public Login getCastedModel() {
		return (Login) this.getModel();
	}

	@Override
	public long getModelID() {
		return this.getCastedModel().getName().hashCode();
	}

	public String getName() {
		return this.getCastedModel().getName();
	}

	public void onClick(final View view) {
		// Toggle location to show its contents.
		this.invalidate();
		this.toggleExpanded();
		this.fireStructureChange(SystemWideConstants.events.EVENTSTRUCTURE_ACTIONEXPANDCOLLAPSE, this, this);
	}

	/**
	 * Sort the Regions by their names.
	 */
	@Override
	public Vector<IPart> runPolicies(final Vector<IPart> targets) {
		// Order the characters by alphabetical name.
		Collections.sort(targets, NeoComApp.createPartComparator(AppWideConstants.comparators.COMPARATOR_NAME));
		return targets;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("LoginPart [");
		buffer.append(this.getCastedModel().getName());
		buffer.append(" ]");
		return buffer.toString();
	}

	@Override
	protected AbstractHolder selectHolder() {
		// Get the proper holder from the render mode.
		return new LoginRender(this, _activity);
	}

	@Override
	protected AbstractRender selectRenderer() {
		// TODO Auto-generated method stub
		return null;
	}
}

//- CLASS IMPLEMENTATION ...................................................................................
final class LoginRender extends EveAbstractHolder {
	// - S T A T I C - S E C T I O N ..........................................................................
	//private static Logger	logger			= Logger.getLogger("RegionHolder");

	// - F I E L D - S E C T I O N ............................................................................

	// - L A Y O U T   F I E L D S
	public TextView	name					= null;
	public TextView	childCount		= null;

	// - L A Y O U T   L A B E L S
	public TextView	titleLabel		= null;
	public TextView	locationLabel	= null;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public LoginRender(final LoginPart target, final Activity context) {
		super(target, context);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	@Override
	public LoginPart getPart() {
		return (LoginPart) super.getPart();
	}

	@Override
	public void initializeViews() {
		super.initializeViews();
		name = (TextView) _convertView.findViewById(R.id.name);
		childCount = (TextView) _convertView.findViewById(R.id.childCount);

		titleLabel = (TextView) _convertView.findViewById(R.id.titleLabel);
		locationLabel = (TextView) _convertView.findViewById(R.id.locationLabel);
	}

	//	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	//	@SuppressWarnings("deprecation")
	@Override
	public void updateContent() {
		super.updateContent();
		name.setText(this.getPart().get_name());
		childCount.setText(this.getPart().get_contentCount());
		_convertView.invalidate();
	}

	@Override
	protected void createView() {
		final LayoutInflater mInflater = (LayoutInflater) this.getContext()
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		// The view is a new view. We have to fill all the items
		_convertView = mInflater.inflate(R.layout.login4list, null);
		_convertView.setTag(this);
	}
}

// - UNUSED CODE ............................................................................................
