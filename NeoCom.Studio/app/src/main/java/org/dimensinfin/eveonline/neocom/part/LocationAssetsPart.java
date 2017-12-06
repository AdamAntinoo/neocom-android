//	PROJECT:        EVEIndustrialist (EVEI)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2014 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API11.
//	DESCRIPTION:		Application helper for Eve Online Industrialists. Will help on Industry and Manufacture.

package org.dimensinfin.eveonline.neocom.part;

import java.util.HashMap;
import java.util.Vector;

import org.dimensinfin.android.mvc.constants.SystemWideConstants;
import org.dimensinfin.android.mvc.core.AbstractAndroidPart;
import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.android.mvc.interfaces.IMenuActionTarget;
import org.dimensinfin.android.mvc.interfaces.IPart;
import org.dimensinfin.eveonline.neocom.NeoComApp;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.activity.PlanetaryOptimizationActivity;
import org.dimensinfin.eveonline.neocom.connector.NeoComAppConnector;
import org.dimensinfin.eveonline.neocom.constant.AppWideConstants;
import org.dimensinfin.eveonline.neocom.model.EveLocation;
import org.dimensinfin.eveonline.neocom.model.ExtendedLocation;
import org.dimensinfin.eveonline.neocom.model.NeoComAsset;
import org.dimensinfin.eveonline.neocom.model.NeoComCharacter;
import org.dimensinfin.eveonline.neocom.render.Location4AssetsRender;
import org.dimensinfin.eveonline.neocom.storage.NeoComModelStore;

import android.content.Intent;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

// - CLASS IMPLEMENTATION ...................................................................................
public class LocationAssetsPart extends LocationPart implements IMenuActionTarget, OnClickListener {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static final long													serialVersionUID	= -1226559463320336724L;
	private static String															_contextMenuTitle	= "Select Location Role";
	private static String[]														_contextMenu			= null;

	// - F I E L D - S E C T I O N ............................................................................
	private final HashMap<Long, AbstractAndroidPart>	containerList			= new HashMap<Long, AbstractAndroidPart>();
	private final HashMap<Long, AssetPart>						containers				= new HashMap();;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public LocationAssetsPart(final EveLocation location) {
		super(location);
		LocationAssetsPart._contextMenu = NeoComApp.getSingletonApp().getResources()
				.getStringArray(R.array.locationFunctions);
	}

	public double getItemsValoration() {
		return itemsValueISK;
	}

	public double getItemsVolume() {
		return itemsVolume;
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	/**
	 * Returns the number of assets located in this Eve Location. There are two values, one for the Location
	 * collapsed that will show the total number of assets that are found on the database and the other values
	 * when the Location is expanded that will show the number of graphical items (these are the real children
	 * on the location) that will hide the number of items inside other containers.
	 *
	 * @return
	 */
	@Override
	public String getTLocationContentCount() {
		int locationAssets = 0;
		if (this.getCastedModel() instanceof ExtendedLocation) {
			locationAssets = ((ExtendedLocation) this.getCastedModel()).getContentSize();
			//		else  locationAssets = this.getCastedModel().getContent();
			//		if (this.getCastedModel().isExpanded()) {
			//			locationAssets = this.getChildren().size();
			//		} else {
			//			NeoComAppConnector.getSingleton().getDBConnector().getLocationContentCount(this.getCastedModel().getID());
			//			
			//				// DEBUG I have to get access to the pilot ID for some filters. Possible set the parent the root and from it to the Pilot
			//				locationAssets = assetDao
			//						.countOf(assetDao.queryBuilder().setCountOf(true).where().eq("ownerID", this.getPilot().getCharacterID())
			//								.and().eq("locationID", this.getCastedModel().getID()).prepare());
			//			} catch (java.sql.SQLException sqle) {
			//				sqle.printStackTrace();
			//			}
			//	}
		}

		String countString = null;

		if (locationAssets > 1) {
			countString = NeoComAbstractPart.qtyFormatter.format(locationAssets) + " items";
		} else {
			countString = NeoComAbstractPart.qtyFormatter.format(locationAssets) + " item";
		}
		return countString;
	}

	public CharSequence getTLocationID() {
		return NeoComAbstractPart.keyFormatter.format(this.getCastedModel().getID());
	}

	/**
	 * This is a lazy evaluation implementation that will access their database contents on the fist call. If
	 * those contents are already on place it will return the signal to update the UI.<br>
	 * During the download of the contents it will detect BPC and process the assets to generate aggregations of
	 * BPC to simplify the interface. It will also calculate the number of real assets (shown when the Location
	 * is collapsed) and the number of children (shown when the Location is expanded).<br>
	 * During the processing the number of contents should be changed to a spinning later replaced by the right
	 * number (expand/collapse).
	 */
	public void onClick(final View view) {
		// Toggle location to show its contents.
		this.invalidate();
		this.toggleExpanded();
		// For lazy evaluted parts check if the toggle requires a download.
		if ((this.getCastedModel().isExpanded()) && (!this.getCastedModel().isDownloaded())) {
			this.fireStructureChange(SystemWideConstants.events.EVENTSTRUCTURE_DOWNLOADDATA.name(), this, this);
		} else {
			this.fireStructureChange(SystemWideConstants.events.EVENTSTRUCTURE_ACTIONEXPANDCOLLAPSE.name(), this, this);
		}
	}

	public boolean onContextItemSelected(final MenuItem item) {
		LocationPart.logger.info(">> [LocationAssetsPart.onContextItemSelected]"); //$NON-NLS-1$
		switch (item.getItemId()) {
			case R.id.context_location_optimize:
				// Managers have a reference to the Character linked when created.
				final NeoComCharacter pilot = NeoComModelStore.getSingleton().getActiveCharacter();
				final Intent intent = new Intent(this.getActivity(), PlanetaryOptimizationActivity.class);
				// Add all the identification chain as in the backend Angular calls. This is Login/Pilot/Manager
				intent.putExtra(AppWideConstants.EExtras.EXTRA_LOGINID.name(), pilot.getLoginRef().getName());
				intent.putExtra(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name(), pilot.getCharacterID());
				intent.putExtra(AppWideConstants.EExtras.EXTRA_LOCATIONID.name(), this.getCastedModel().getID());
				NeoComModelStore.getSingleton().getActivity().startActivity(intent);
				return true;
			default:
				// Pass the event to parent, for options menu, use below
				//		    return super.onOptionsItemSelected(item);
		}
		LocationPart.logger.info("<< [LocationAssetsPart.onContextItemSelected]"); //$NON-NLS-1$
		return true;
	}

	public void onCreateContextMenu(final ContextMenu menu, final View view, final ContextMenuInfo menuInfo) {
		LocationPart.logger.info(">> [LocationAssetsPart.onCreateContextMenu]"); //$NON-NLS-1$
		// Initialize the contextual menu each time.
		MenuInflater inflater = new MenuInflater(NeoComAppConnector.getSingleton().getApplicationContext());
		inflater.inflate(R.menu.context_location4assets, menu);
		LocationPart.logger.info("<< [LocationAssetsPart.onCreateContextMenu]"); //$NON-NLS-1$
	}

	@Override
	protected AbstractRender selectRenderer() {
		// Get the proper holder from the render mode.
		return new Location4AssetsRender(this, _activity);
	}

	/**
	 * Search for this container reference on this Location's children until found. Then aggregates the asset to
	 * that container calculating stacking if this is possible. There can be containers inside container like
	 * the case where a container is on the hols of a ship. That special case will not be implemented on this
	 * first approach and all the container will be located at the Location's hangar floor.<br>
	 * Containers also do not have its market value added to the location's aggregation.
	 *
	 * @param apart
	 */
	private void add2Container(final AssetPart apart) {
		Log.i("LocationAssetsPart", ">> LocationAssetsPart.add2Container");
		// Locate the container if already added to the location.
		long pcid = apart.getCastedModel().getParentContainer().getDAOID();
		Vector<IPart> childs = this.getChildren();
		for (IPart child : childs)
			if (child instanceof AssetPart) {
				AssetPart check = (AssetPart) child;
				long ccid = check.getCastedModel().getDAOID();
				if (ccid == pcid) {
					if (apart.getCastedModel().isBlueprint()) {
						check.checkAssetStacking(apart);
					} else {
						check.addChild(apart);
					}
					Log.i("LocationAssetsPart", ".. LocationAssetsPart.add2Container added to container: " + ccid);
					return;
				}
			}
		// Add the container to the location and the the item to the container.
		NeoComAsset container = apart.getCastedModel().getParentContainer();
		AssetPart newpart = null;
		if (container.isShip()) {
			newpart = (AssetPart) new ShipPart(container).setRenderMode(this.getRenderMode());
		} else if (container.isContainer()) {
			newpart = (AssetPart) new ContainerPart(container).setRenderMode(this.getRenderMode());
		} else {
			newpart = (AssetPart) new AssetPart(container).setRenderMode(this.getRenderMode());
		}
		this.addChild(newpart);
		newpart.addChild(apart);
		Log.i("LocationAssetsPart", ".. LocationAssetsPart.add2Container created new container: " + container.getDAOID());
	}

	private void add2Location(final AssetPart apart) {
		//	if (apart instanceof AssetPart) {
		// Stacking is only for BPC
		//	if (apart instanceof AssetPart)
		if (apart.getCastedModel().isBlueprint()) {
			this.checkAssetStacking(apart);
		} else {
			this.addChild(apart);
			//	}
		}
	}
}

// - UNUSED CODE ............................................................................................
