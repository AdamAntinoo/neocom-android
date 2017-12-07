//	PROJECT:        NeoCom.Android (NEOC.A)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2017 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API16.
//	DESCRIPTION:		Application to get access to CCP api information and help manage industrial activities
//									for characters and corporations at Eve Online. The set is composed of some projects
//									with implementation for Android and for an AngularJS web interface based on REST
//									services on Sprint Boot Cloud.
package org.dimensinfin.eveonline.neocom.part;

import java.util.ArrayList;

import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.android.mvc.interfaces.INamedPart;
import org.dimensinfin.android.mvc.interfaces.IPart;
import org.dimensinfin.core.model.AbstractComplexNode;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.activity.PlanetaryManagerActivity;
import org.dimensinfin.eveonline.neocom.constant.AppWideConstants;
import org.dimensinfin.eveonline.neocom.enums.EManagerType;
import org.dimensinfin.eveonline.neocom.interfaces.INeoComDirectorPart;
import org.dimensinfin.eveonline.neocom.manager.PlanetaryManager;
import org.dimensinfin.eveonline.neocom.model.NeoComCharacter;
import org.dimensinfin.eveonline.neocom.render.PlanetaryManagerRender;
import org.dimensinfin.eveonline.neocom.storage.NeoComModelStore;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

// - CLASS IMPLEMENTATION ...................................................................................
public class PlanetaryManagerPart extends NeoComAbstractPart
		implements INeoComDirectorPart, INamedPart, OnClickListener/* , OnLongClickListener */ {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static final long serialVersionUID = 7725920841939916584L;

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public PlanetaryManagerPart(final AbstractComplexNode model) {
		super(model);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	/**
	 * Returns if this Manager is active for the current character. By default all managers are active for all
	 * Characters.
	 */
	public boolean checkActivation() {
		return true;
	}

	/**
	 * The Managers do not expand so their collaboration is only themselves.
	 */
	@Override
	public ArrayList<IPart> collaborate2View() {
		return new ArrayList<IPart>();
	}

	public int getActiveIcon() {
		return R.drawable.planets;
	}

	public PlanetaryManager getCastedModel() {
		return (PlanetaryManager) this.getModel();
	}

	public int getDimmedIcon() {
		return R.drawable.planets;
	}

	@Override
	public long getModelID() {
		return PlanetaryManagerPart.serialVersionUID;
	}

	public String getName() {
		return "Planetary Assets";
	}

	/**
	 * Returns the number of assets that belong to this Character related to the number of records on the local
	 * copy at the database.
	 * 
	 * @return
	 */
	public String getTAssetsCount() {
		return NeoComAbstractPart.itemCountFormatter.format(this.getCastedModel().getAssetTotalCount());
	}

	public String getTRegionCount() {
		return NeoComAbstractPart.itemCountFormatter.format(this.getCastedModel().getRegions().size());
	}

	/**
	 * A click on the Manager should trigger the expand/collapse pattern.
	 */
	public void onClick(final View view) {
		NeoComAbstractPart.logger.info(">> [PilotInfoPart.onClick]");
		final Object managerPart = view.getTag();
		if (managerPart instanceof PlanetaryManagerPart) {
			// Managers have a reference to the Character linked when created.
			final NeoComCharacter pilot = ((PlanetaryManagerPart) managerPart).getCastedModel().getPilot();
			final Intent intent = new Intent(this.getActivity(), PlanetaryManagerActivity.class);
			// Add all the identification chain as in the backend Angular calls. This is Login/Pilot/Manager
			intent.putExtra(AppWideConstants.EExtras.EXTRA_LOGINID.name(), pilot.getLoginRef().getName());
			intent.putExtra(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name(), pilot.getCharacterID());
			intent.putExtra(AppWideConstants.EExtras.EXTRA_MANAGERNAME.name(), EManagerType.PLANETARY_MANAGER.name());
			NeoComModelStore.getSingleton().getActivity().startActivity(intent);
		}
		NeoComAbstractPart.logger.info("<< [PilotInfoPart.onClick]");
	}

	//	/**
	//	 * A long click should activate the Manager associated activity.
	//	 */
	//	public boolean onLongClick(final View v) {
	//		return false;
	//	}

	@Override
	protected AbstractRender selectRenderer() {
		return new PlanetaryManagerRender(this, _activity);
	}
}

// - UNUSED CODE ............................................................................................
