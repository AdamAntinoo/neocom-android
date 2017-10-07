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

import org.dimensinfin.android.mvc.core.AbstractHolder;
import org.dimensinfin.android.mvc.interfaces.INamedPart;
import org.dimensinfin.android.mvc.interfaces.IPart;
import org.dimensinfin.core.model.AbstractComplexNode;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.activity.AssetsDirectorActivity;
import org.dimensinfin.eveonline.neocom.connector.AppConnector;
import org.dimensinfin.eveonline.neocom.constant.AppWideConstants;
import org.dimensinfin.eveonline.neocom.core.EveAbstractPart;
import org.dimensinfin.eveonline.neocom.enums.EManagerType;
import org.dimensinfin.eveonline.neocom.interfaces.INeoComDirectorPart;
import org.dimensinfin.eveonline.neocom.manager.AssetsManager;
import org.dimensinfin.eveonline.neocom.model.NeoComCharacter;
import org.dimensinfin.eveonline.neocom.render.AssetsManagerRender;
import org.dimensinfin.eveonline.neocom.storage.AppModelStore;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;

// - CLASS IMPLEMENTATION ...................................................................................
public class AssetsManagerPart extends EveAbstractPart
		implements INeoComDirectorPart, INamedPart, OnClickListener, OnLongClickListener {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static final long serialVersionUID = -8992564305513190092L;

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public AssetsManagerPart(final AbstractComplexNode model) {
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

	@Override
	public ArrayList<IPart> collaborate2View() {
		return super.collaborate2View();
	}

	/**
	 * Returns the number of assets that belong to this Character related to the number of records on the local
	 * copy at the database.
	 * 
	 * @return
	 */
	public String get_assetsCount() {
		return EveAbstractPart.itemCountFormatter.format(this.getCastedModel().getAssetTotalCount());
	}

	public String get_regionCount() {
		return EveAbstractPart.itemCountFormatter.format(this.getCastedModel().getRegions().size());
	}

	public int getActiveIcon() {
		return R.drawable.assets;
	}

	public AssetsManager getCastedModel() {
		return (AssetsManager) this.getModel();
	}

	public int getDimmedIcon() {
		return R.drawable.assetsdimmed;
	}

	public String getName() {
		return "Assets";
	}

	/**
	 * If the Pilot is active the click has to show the Pilot Dashboard Activity. If the Pilot is not active the
	 * event should be discarded.<br>
	 * If this part is used on more places we should control the variant so the click can be active for ones and
	 * not active for others.
	 */
	public void onClick(final View view) {
		EveAbstractPart.logger.info(">> [PilotInfoPart.onClick]");
		// Set the pilot selected on the context and then go to the Pilot Dashboard.
		final Object pilotPart = view.getTag();
		if (pilotPart instanceof AssetsManagerPart) {
			// TODO This is to keep compatibility with the old data management.
			// Pilot are expected to be at the global context
			final NeoComCharacter pilot = AppConnector.getModelStore().getActiveCharacter();
			// TODO This should be done at the destination Activity
			//	AppConnector.getModelStore().activatePilot(pilot.getCharacterID());
			//			final Intent intent = new Intent(this.getActivity(), DirectorsBoardActivity.class);
			final Intent intent = new Intent(this.getActivity(), AssetsDirectorActivity.class);
			// Add all the identification chain as in the backend Angular calls. This is Login/Pilot
			//	intent.putExtra(AppWideConstants.EExtras.EXTRA_LOGINID.name(), pilot.getLoginRef().getName());
			intent.putExtra(AppWideConstants.EExtras.EXTRA_LOGINID.name(), pilot.getLoginRef().getName());
			intent.putExtra(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name(), pilot.getCharacterID());
			intent.putExtra(AppWideConstants.EExtras.EXTRA_MANAGERNAME.name(), EManagerType.ASSETS_MANAGER.name());
			AppModelStore.getSingleton().getActivity().startActivity(intent);
		}
		EveAbstractPart.logger.info("<< [PilotInfoPart.onClick]");
	}

	/**
	 * A long click should activate the Manager associated activity.
	 */
	public boolean onLongClick(final View v) {
		return false;
	}

	@Override
	protected AbstractHolder selectHolder() {
		return new AssetsManagerRender(this, _activity);
	}
}

// - UNUSED CODE ............................................................................................
//	/**
//	 * The policies for locations are two sorts. The first one by name the second by item type.
//	 */
//	@Override
//	public Vector<IPart> runPolicies(final Vector<IPart> targets) {
//		Collections.sort(targets, NeoComApp.createPartComparator(AppWideConstants.comparators.COMPARATOR_NAME));
//		Collections.sort(targets, NeoComApp.createPartComparator(AppWideConstants.comparators.COMPARATOR_ITEM_TYPE));
//		return targets;
//	}

//	public void setContainerLocation(final boolean flag) {
//		isContainer = flag;
//	}
//
//	public void setContainerName(final String name) {
//		containerName = name;
//	}

//public void startActivity(final Intent intent) {
//// TODO Auto-generated method stub
//
//}
