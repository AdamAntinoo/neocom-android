//	PROJECT:        NeoCom.Android (NEOC.A)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2018 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API22.
//	DESCRIPTION:		Android Application related to the Eve Online game. The purpose is to download and organize
//                  the game data to help capsuleers organize and prioritize activities. The strong points are
//                  help at the Industry level tracking and calculating costs and benefits. Also the market
//                  information update service will help to identify best prices and locations.
//                  Planetary Interaction and Ship fittings are point under development.
//                  ESI authorization is a new addition that will give continuity and allow download game data
//                  from the new CCP data services.
//                  This is the Android application version but shares libraries and code with other application
//                  designed for Spring Boot Angular 4 platform.
//                  The model management is shown using a generic Model View Controller that allows make the
//                  rendering of the model data similar on all the platforms used.
package org.dimensinfin.eveonline.neocom.part;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.android.mvc.interfaces.INamedPart;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.activity.PlanetaryManagerActivity;
import org.dimensinfin.eveonline.neocom.constant.AppWideConstants;
import org.dimensinfin.eveonline.neocom.interfaces.INeoComDirectorPart;
import org.dimensinfin.eveonline.neocom.manager.PlanetaryManager;
import org.dimensinfin.eveonline.neocom.model.NeoComCharacter;
import org.dimensinfin.eveonline.neocom.part.core.NeoComAbstractPart;
import org.dimensinfin.eveonline.neocom.render.NeoComRender;
import org.dimensinfin.eveonline.neocom.storage.NeoComModelStore;

// - CLASS IMPLEMENTATION ...................................................................................
public class PlanetaryManagerPart extends NeoComAbstractPart
		implements INeoComDirectorPart, INamedPart, OnClickListener {
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

	/**
	 * A click on the Manager should trigger the expand/collapse pattern.
	 */
	public void onClick (final View view) {
		NeoComAbstractPart.logger.info(">> [PlanetaryManagerPart.onClick]");
		final Object managerPart = view.getTag();
		if ( managerPart instanceof PlanetaryManagerPart ) {
			// Managers have a reference to the Character linked when created.
			final NeoComCharacter pilot = ((PlanetaryManagerPart) managerPart).getCastedModel().getPilot();
			final Intent intent = new Intent(this.getActivity(), PlanetaryManagerActivity.class);
			// Add all the identification chain as in the backend Angular calls. This is Login/Pilot/Manager
			intent.putExtra(AppWideConstants.EExtras.EXTRA_LOGINID.name(), pilot.getLoginRef().getName());
			intent.putExtra(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name(), pilot.getCharacterID());
			NeoComModelStore.getSingleton().getActivity().startActivity(intent);
		}
		NeoComAbstractPart.logger.info("<< [PlanetaryManagerPart.onClick]");
	}

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
			nodeIcon.setImageDrawable(this.getContext().getResources().getDrawable(this.getPart().getActiveIcon(), getContext().getTheme()));
		} else {
			nodeIcon.setImageDrawable(this.getContext().getResources().getDrawable(this.getPart().getDimmedIcon(), getContext().getTheme()));
		}
		// Check if the click is active. If not change the border to gray.
		if ( getPart() instanceof OnClickListener )
			_convertView.setBackground(getContext().getResources().getDrawable(R.drawable.panelbordergreen, getContext().getTheme()));
		else
			_convertView.setBackground(getContext().getResources().getDrawable(R.drawable.panelbordergray, getContext().getTheme()));
		_convertView.invalidate();
	}

	@Override
	protected void createView () {
		_convertView = inflateView(R.layout.planetarymanager4pilot);
		_convertView.setTag(this);
	}
}

// - UNUSED CODE ............................................................................................
