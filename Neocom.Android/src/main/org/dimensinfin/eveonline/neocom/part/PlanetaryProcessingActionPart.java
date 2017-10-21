//	PROJECT:        NeoCom.Android (NEOC.A)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2017 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API16.
//	DESCRIPTION:		Application to get access to CCP api information and help manage industrial activities
//									for characters and corporations at Eve Online. The set is composed of some projects
//									with implementation for Android and for an AngularJS web interface based on REST
//									services on Sprint Boot Cloud.
package org.dimensinfin.eveonline.neocom.part;

import java.util.Vector;

import org.dimensinfin.android.mvc.core.AbstractAndroidPart;
import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.android.mvc.interfaces.IPart;
import org.dimensinfin.core.model.AbstractComplexNode;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.core.EveAbstractHolder;
import org.dimensinfin.eveonline.neocom.industry.Resource;
import org.dimensinfin.eveonline.neocom.planetary.ProcessingAction;
import org.dimensinfin.eveonline.neocom.refactoring.AExtendedPart;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

// - CLASS IMPLEMENTATION ...................................................................................
public class PlanetaryProcessingActionPart
		extends AExtendedPart /* implements IItemPart, OnClickListener, IMenuActionTarget */ {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static final long serialVersionUID = -2155103853531229362L;

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public PlanetaryProcessingActionPart(final AbstractComplexNode node) {
		super(node);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	@Override
	public ProcessingAction getCastedModel() {
		return (ProcessingAction) this.getModel();
	}

	@Override
	public long getModelID() {
		return this.getCastedModel().getTargetId();
	}

	/**
	 * The default actions inside this method usually are the sorting of the children nodes. For actions there
	 * is not sorting required so do nothing with the input.
	 */
	@Override
	public Vector<IPart> runPolicies(final Vector<IPart> targets) {
		return targets;
	}

	@Override
	public String toString() {
		final StringBuffer buffer = new StringBuffer("PlanetaryProcessingActionPart [");
		final String action = this.getCastedModel().toString();
		buffer.append(action);
		buffer.append("]");
		return buffer.toString();
	}

	@Override
	protected AbstractRender selectRenderer() {
		return new PlanetaryProcessingActionRender(this, _activity);
	}
}

//- CLASS IMPLEMENTATION ...................................................................................
final class PlanetaryActionResourceRender extends EveAbstractHolder {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................
	// - L A Y O U T   F I E L D S
	private ImageView	itemIcon						= null;
	private TextView	resourceName				= null;
	private TextView	resourceQuantity		= null;
	private TextView	resourcePrice				= null;
	private TextView	resourceTotalValue	= null;

	// - L A Y O U T   L A B E L S
	//	private final TextView	assetsCountLabel	= null;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public PlanetaryActionResourceRender(final AbstractAndroidPart newPart, final Activity context) {
		super(newPart, context);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	@Override
	public ResourcePart getPart() {
		return (ResourcePart) super.getPart();
	}

	@Override
	public void initializeViews() {
		super.initializeViews();
		itemIcon = (ImageView) _convertView.findViewById(R.id.itemIcon);
		resourceName = (TextView) _convertView.findViewById(R.id.resourceName);
		resourceQuantity = (TextView) _convertView.findViewById(R.id.resourceQuantity);
		resourcePrice = (TextView) _convertView.findViewById(R.id.resourcePrice);
		resourceTotalValue = (TextView) _convertView.findViewById(R.id.resourceTotalValue);
	}

	/**
	 * This Render has some sections. There are the aggregation sections both for inputs and for outputs and the
	 * specific section for the list of input and another for the list of outputs that should be handled
	 * manually by code and not with the common MVC patterns.<br>
	 * We can explode the Resource layouts and then put them inside the corresponding holders to complete the
	 * rendering of the content.
	 */
	@Override
	public void updateContent() {
		super.updateContent();
		resourceName.setText(1234);
		resourceQuantity.setText(4321);
		resourcePrice.setText(4321);
		resourceTotalValue.setText(4321);
	}

	@Override
	protected void createView() {
		final LayoutInflater mInflater = (LayoutInflater) this.getContext()
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		_convertView = mInflater.inflate(R.layout.planetaryactionresource, null);
		_convertView.setTag(this);
	}
}

//- CLASS IMPLEMENTATION ...................................................................................
final class PlanetaryProcessingActionRender extends EveAbstractHolder {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................
	// - L A Y O U T   F I E L D S
	private TextView				inputsValueTotal			= null;
	private TextView				outputsValueTotal			= null;
	private LinearLayout		inputsResourcesList		= null;
	private LinearLayout		outputsResourcesList	= null;

	// - L A Y O U T   F I E L D S   F O R   R E S O U R C E
	private final ImageView	itemIcon							= null;
	private final TextView	resourceName					= null;
	private final TextView	resourceQuantity			= null;
	private final TextView	resourcePrice					= null;
	private final TextView	resourceTotalValue		= null;

	// - L A Y O U T   L A B E L S
	//	private final TextView	assetsCountLabel	= null;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public PlanetaryProcessingActionRender(final AbstractAndroidPart newPart, final Activity context) {
		super(newPart, context);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	@Override
	public PlanetaryProcessingActionPart getPart() {
		return (PlanetaryProcessingActionPart) super.getPart();
	}

	@Override
	public void initializeViews() {
		super.initializeViews();
		inputsValueTotal = (TextView) _convertView.findViewById(R.id.inputsValueTotal);
		outputsValueTotal = (TextView) _convertView.findViewById(R.id.outputsValueTotal);
		inputsResourcesList = (LinearLayout) _convertView.findViewById(R.id.inputsResourcesList);
		outputsResourcesList = (LinearLayout) _convertView.findViewById(R.id.outputsResourcesList);
	}

	/**
	 * This Render has some sections. There are the aggregation sections both for inputs and for outputs and the
	 * specific section for the list of input and another for the list of outputs that should be handled
	 * manually by code and not with the common MVC patterns.<br>
	 * We can explode the Resource layouts and then put them inside the corresponding holders to complete the
	 * rendering of the content.
	 */
	@Override
	public void updateContent() {
		super.updateContent();
		inputsValueTotal.setText(1234);
		outputsValueTotal.setText(4321);

		final LayoutInflater mInflater = (LayoutInflater) this.getContext()
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

		// Add the resources to the resources layout. Inputs
		PlanetaryActionResourceRender resource = new PlanetaryActionResourceRender(new ResourcePart(new Resource(20)),
				this.getContext());
		inputsResourcesList.addView(resource.getView());

		// Add the resources to the resources layout. Outputs
		resource = new PlanetaryActionResourceRender(new ResourcePart(new Resource(20)), this.getContext());
		inputsResourcesList.addView(resource.getView());
	}

	@SuppressLint("InflateParams")
	@Override
	protected void createView() {
		final LayoutInflater mInflater = (LayoutInflater) this.getContext()
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		_convertView = mInflater.inflate(R.layout.planetaryaction, null);
		_convertView.setTag(this);
	}
}

// - UNUSED CODE ............................................................................................
