//	PROJECT:        NeoCom.Android (NEOC.A)
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
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.android.mvc.interfaces.INamedPart;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.interfaces.INeoComDirectorPart;
import org.dimensinfin.eveonline.neocom.manager.PlanetaryManager;
import org.dimensinfin.eveonline.neocom.part.core.NeoComAbstractPart;
import org.dimensinfin.eveonline.neocom.render.NeoComRender;

// - CLASS IMPLEMENTATION ...................................................................................
public class PlanetaryManagerPart extends NeoComAbstractPart
		implements INeoComDirectorPart, INamedPart/*, OnClickListener*//* , OnLongClickListener */ {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static final long serialVersionUID = 7725920841939916584L;

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public PlanetaryManagerPart (final PlanetaryManager model) {
		super(model);
	}

	// - M E T H O D - S E C T I O N ..........................................................................

	/**
	 * Returns if this Manager is active for the current character. By default all managers are active for all
	 * Characters.
	 */
	public boolean checkActivation () {
		return true;
	}

	//	/**
	//	 * The Managers do not expand so their collaboration is only themselves.
	//	 */
	//	@Override
	//	public ArrayList<IPart> collaborate2View() {
	//		return new ArrayList<IPart>();
	//	}

	public int getActiveIcon () {
		return R.drawable.planets;
	}

	public PlanetaryManager getCastedModel () {
		return (PlanetaryManager) this.getModel();
	}

	public int getDimmedIcon () {
		return R.drawable.planets;
	}

	@Override
	public long getModelID () {
		return PlanetaryManagerPart.serialVersionUID;
	}

	public String getName () {
		return "Planetary Assets";
	}

	/**
	 * Returns the number of planetary resources that belong to this Character. This data is obtained from the
	 * <code>PlanetaryManager</code> after checking the database records.
	 */
	public String getTResourcesCount () {
		return NeoComAbstractPart.itemCountFormatter.format(this.getCastedModel().getAssetTotalCount());
	}
	//
	//	public String getTRegionCount() {
	//		return NeoComAbstractPart.itemCountFormatter.format(this.getCastedModel().getRegions().size());
	//	}

	//	/**
	//	 * A click on the Manager should trigger the expand/collapse pattern.
	//	 */
	//	public void onClick(final View view) {
	//		NeoComAbstractPart.logger.info(">> [PilotInfoPart.onClick]");
	//		final Object managerPart = view.getTag();
	//		if (managerPart instanceof PlanetaryManagerPart) {
	//			// Managers have a reference to the Character linked when created.
	//			final NeoComCharacter pilot = ((PlanetaryManagerPart) managerPart).getCastedModel().getPilot();
	//			final Intent intent = new Intent(this.getActivity(), PlanetaryManagerActivity.class);
	//			// Add all the identification chain as in the backend Angular calls. This is Login/Pilot/Manager
	//			intent.putExtra(AppWideConstants.EExtras.EXTRA_LOGINID.name(), pilot.getLoginRef().getName());
	//			intent.putExtra(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name(), pilot.getCharacterID());
	//			intent.putExtra(AppWideConstants.EExtras.EXTRA_MANAGERNAME.name(), EManagerType.PLANETARY_MANAGER.name());
	//			NeoComModelStore.getSingleton().getActivity().startActivity(intent);
	//		}
	//		NeoComAbstractPart.logger.info("<< [PilotInfoPart.onClick]");
	//	}

	//	/**
	//	 * A long click should activate the Manager associated activity.
	//	 */
	//	public boolean onLongClick(final View v) {
	//		return false;
	//	}

	@Override
	protected AbstractRender selectRenderer () {
		return new PlanetaryManagerRender(this, _activity);
	}
}

// - CLASS IMPLEMENTATION ...................................................................................
final class PlanetaryManagerRender extends NeoComRender {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................

	// - L A Y O U T   F I E L D S
	private ImageView nodeIcon = null;
	private TextView name = null;
	private TextView resourceCount = null;
	//	private TextView regionCount = null;

	// - L A Y O U T   L A B E L S

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public PlanetaryManagerRender (final NeoComAbstractPart target, final Activity context) {
		super(target, context);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	@Override
	public PlanetaryManagerPart getPart () {
		return (PlanetaryManagerPart) super.getPart();
	}

	@Override
	public void initializeViews () {
		super.initializeViews();
		nodeIcon = (ImageView) _convertView.findViewById(R.id.nodeIcon);
		name = (TextView) _convertView.findViewById(R.id.name);
		resourceCount = (TextView) _convertView.findViewById(R.id.resourceCount);
		//		regionCount = (TextView) _convertView.findViewById(R.id.managerRegionCount);
	}

	@Override
	public void updateContent () {
		super.updateContent();
		name.setText(this.getPart().getName());
		resourceCount.setText(this.getPart().getTResourcesCount());
		//		regionCount.setText(this.getPart().getTRegionCount());
		// Needs the check to test of the manager is active to select the right icon.
		if ( this.getPart().checkActivation() ) {
			nodeIcon.setImageDrawable(this.getContext().getResources().getDrawable(this.getPart().getActiveIcon()));
		} else {
			nodeIcon.setImageDrawable(this.getContext().getResources().getDrawable(this.getPart().getDimmedIcon()));
		}
		//		_convertView.setBackgroundResource(R.drawable.topwhiteline);
	}

	@Override
	protected void createView () {
		final LayoutInflater mInflater = (LayoutInflater) this.getContext()
		                                                      .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		// The view is a new view. We have to fill all the items
		_convertView = mInflater.inflate(R.layout.planetarymanager4pilot, null);
		_convertView.setTag(this);
	}
}

// - UNUSED CODE ............................................................................................
