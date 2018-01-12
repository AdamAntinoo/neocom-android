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
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.dimensinfin.android.mvc.constants.SystemWideConstants;
import org.dimensinfin.android.mvc.core.AbstractAndroidPart;
import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.android.mvc.core.OneShotTask;
import org.dimensinfin.core.constant.CoreConstants;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.enums.ELocationType;
import org.dimensinfin.eveonline.neocom.model.EveLocation;
import org.dimensinfin.eveonline.neocom.model.ExtendedLocation;
import org.dimensinfin.eveonline.neocom.part.core.NeoComAbstractPart;
import org.dimensinfin.eveonline.neocom.part.core.NeoComExpandablePart;
import org.dimensinfin.eveonline.neocom.render.BaseExpandableRender;
import org.dimensinfin.eveonline.neocom.render.NeoComRender;

import java.util.Hashtable;

// - CLASS IMPLEMENTATION ...................................................................................
public class Location4AssetsPart extends LocationPart {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static final long serialVersionUID = -1226559463320336724L;
	private static String _contextMenuTitle = "Select Location Role";
	private static String[] _contextMenu = null;

	// - F I E L D - S E C T I O N ............................................................................
	private final Hashtable<Long, AbstractAndroidPart> containerList = new Hashtable<Long, AbstractAndroidPart>();
	//	private final Hashtable<Long, AssetPart>						containers				= new Hashtable();;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public Location4AssetsPart (final EveLocation location) {
		super(location);
		// Set the separator color.
		setFlavour(ColorFlavour.ORANGE);
		//		_contextMenu = NeoComApp.getSingletonApp().getResources()
		//				.getStringArray(R.array.locationFunctions);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public double getItemsValoration () {
		return itemsValueISK;
	}

	public double getItemsVolume () {
		return itemsVolume;
	}

	/**
	 * Returns the number of assets located in this Eve Location. There are two values, one for the Location
	 * collapsed that will show the total number of assets that are found on the database and the other values
	 * when the Location is expanded that will show the number of graphical items (these are the real children
	 * on the location) that will hide the number of items inside other containers.
	 */
	@Override
	public String getTLocationContentCount () {
		int locationAssets = 0;
		if ( this.getCastedModel() instanceof ExtendedLocation ) {
			locationAssets = ((ExtendedLocation) this.getCastedModel()).getContentSize();
		}
		String countString = null;
		if ( locationAssets > 1 ) {
			countString = NeoComAbstractPart.qtyFormatter.format(locationAssets) + " items";
		} else {
			countString = NeoComAbstractPart.qtyFormatter.format(locationAssets) + " item";
		}
		return countString;
	}

	public CharSequence getTLocationID () {
		return "[" + NeoComAbstractPart.keyFormatter.format(this.getCastedModel().getID()) + "]";
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
	public void onClick (final View view) {
		logger.info(">> [Location4AssetsPart.onClick]");
		// Do the standard expand/collapse and the fire another message for the update of the model.
		super.onClick(view);
		new Handler().postDelayed(new OneShotTask<NeoComExpandablePart>(this) {
			public void run () {
				logger.info(">> [Location4AssetsPart.onClick.OneShotTask.run]");
				getTarget().fireStructureChange(SystemWideConstants.events.EVENTSTRUCTURE_DOWNLOADDATA.name(), this, this);
				logger.info("<< [Location4AssetsPart.onClick.OneShotTask.run]");
			}
		}, CoreConstants.HUNDRETH);
		logger.info("<< [Location4AssetsPart.onClick]");
	}

	//	public boolean onContextItemSelected (final MenuItem item) {
	//		LocationPart.logger.info(">> [LocationAssetsPart.onContextItemSelected]"); //$NON-NLS-1$
	//		switch (item.getItemId()) {
	//			case R.id.context_location_optimize:
	//				// Managers have a reference to the Character linked when created.
	//				final NeoComCharacter pilot = NeoComModelStore.getSingleton().getActiveCharacter();
	//				final Intent intent = new Intent(this.getActivity(), PlanetaryOptimizationActivity.class);
	//				// Add all the identification chain as in the backend Angular calls. This is Login/Pilot/Manager
	//				intent.putExtra(AppWideConstants.EExtras.EXTRA_LOGINID.name(), pilot.getLoginRef().getName());
	//				intent.putExtra(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name(), pilot.getCharacterID());
	//				intent.putExtra(AppWideConstants.EExtras.EXTRA_LOCATIONID.name(), this.getCastedModel().getID());
	//				NeoComModelStore.getSingleton().getActivity().startActivity(intent);
	//				return true;
	//			default:
	//				// Pass the event to parent, for options menu, use below
	//				//		    return super.onOptionsItemSelected(item);
	//		}
	//		LocationPart.logger.info("<< [LocationAssetsPart.onContextItemSelected]"); //$NON-NLS-1$
	//		return true;
	//	}
	//
	//	public void onCreateContextMenu (final ContextMenu menu, final View view, final ContextMenuInfo menuInfo) {
	//		LocationPart.logger.info(">> [LocationAssetsPart.onCreateContextMenu]"); //$NON-NLS-1$
	//		// Initialize the contextual menu each time.
	//		MenuInflater inflater = new MenuInflater(NeoComAppConnector.getSingleton().getApplicationContext());
	//		inflater.inflate(R.menu.context_location4assets, menu);
	//		LocationPart.logger.info("<< [LocationAssetsPart.onCreateContextMenu]"); //$NON-NLS-1$
	//	}

	@Override
	protected AbstractRender selectRenderer () {
		// Get the proper holder from the render mode.
		return new Location4AssetsRender(this, _activity);
	}

	//	/**
	//	 * Search for this container reference on this Location's children until found. Then aggregates the asset to
	//	 * that container calculating stacking if this is possible. There can be containers inside container like
	//	 * the case where a container is on the hols of a ship. That special case will not be implemented on this
	//	 * first approach and all the container will be located at the Location's hangar floor.<br>
	//	 * Containers also do not have its market value added to the location's aggregation.
	//	 */
	//	private void add2Container (final AssetPart apart) {
	//		Log.i("LocationAssetsPart", ">> LocationAssetsPart.add2Container");
	//		// Locate the container if already added to the location.
	//		long pcid = apart.getCastedModel().getParentContainer().getDAOID();
	//		Vector<IPart> childs = this.getChildren();
	//		for (IPart child : childs)
	//			if ( child instanceof AssetPart ) {
	//				AssetPart check = (AssetPart) child;
	//				long ccid = check.getCastedModel().getDAOID();
	//				if ( ccid == pcid ) {
	//					if ( apart.getCastedModel().isBlueprint() ) {
	//						check.checkAssetStacking(apart);
	//					} else {
	//						check.addChild(apart);
	//					}
	//					Log.i("LocationAssetsPart", ".. LocationAssetsPart.add2Container added to container: " + ccid);
	//					return;
	//				}
	//			}
	//		// Add the container to the location and the the item to the container.
	//		NeoComAsset container = apart.getCastedModel().getParentContainer();
	//		AssetPart newpart = null;
	//		if ( container.isShip() ) {
	//			newpart = (AssetPart) new ShipPart(container).setRenderMode(this.getRenderMode());
	//		} else if ( container.isContainer() ) {
	//			newpart = (AssetPart) new ContainerPart(container).setRenderMode(this.getRenderMode());
	//		} else {
	//			newpart = (AssetPart) new AssetPart(container).setRenderMode(this.getRenderMode());
	//		}
	//		this.addChild(newpart);
	//		newpart.addChild(apart);
	//		Log.i("LocationAssetsPart", ".. LocationAssetsPart.add2Container created new container: " + container.getDAOID());
	//	}
	//
	//	private void add2Location (final AssetPart apart) {
	//		//	if (apart instanceof AssetPart) {
	//		// Stacking is only for BPC
	//		//	if (apart instanceof AssetPart)
	//		if ( apart.getCastedModel().isBlueprint() ) {
	//			this.checkAssetStacking(apart);
	//		} else {
	//			this.addChild(apart);
	//			//	}
	//		}
	//	}
}

// - CLASS IMPLEMENTATION ...................................................................................
final class Location4AssetsRender extends BaseExpandableRender {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................

	// - L A Y O U T   F I E L D S
	//	protected ImageView nodeIcon = null;
	protected TextView locationRegion = null;
	protected TextView locationSystem = null;
	protected TextView locationIdentifier = null;
	protected TextView locationStation = null;
	protected TextView childCount = null;
	protected TextView function = null;
	protected TextView locationItemsValue = null;
	protected TextView locationItemsVolume = null;

	// - L A Y O U T   L A B E L S
	protected TextView locationItemsValueLabel = null;
	protected TextView locationItemsVolumeLabel = null;
	protected TextView functionLabel = null;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public Location4AssetsRender (final NeoComAbstractPart target, final Activity context) {
		super(target, context);
	}

	public String generatePriceSuffix (final double price) {
		if ( Math.abs(price) > 1200000000.0 ) return "B ISK";
		if ( Math.abs(price) > 12000000.0 ) return "M ISK";
		return "ISK";
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	@Override
	public Location4AssetsPart getPart () {
		return (Location4AssetsPart) super.getPart();
	}

	@Override
	public void initializeViews () {
		super.initializeViews();
		locationRegion = (TextView) _convertView.findViewById(R.id.locationRegion);
		locationSystem = (TextView) _convertView.findViewById(R.id.locationSystem);
		locationIdentifier = (TextView) _convertView.findViewById(R.id.locationIdentifier);
		locationStation = (TextView) _convertView.findViewById(R.id.locationStation);
		childCount = (TextView) _convertView.findViewById(R.id.childCount);
		function = (TextView) _convertView.findViewById(R.id.role);
		locationItemsValue = (TextView) _convertView.findViewById(R.id.locationItemsValue);
		locationItemsVolume = (TextView) _convertView.findViewById(R.id.locationItemsVolume);

		locationItemsValueLabel = (TextView) _convertView.findViewById(R.id.locationItemsValueLabel);
		locationItemsVolumeLabel = (TextView) _convertView.findViewById(R.id.locationItemsVolumeLabel);
		functionLabel = (TextView) _convertView.findViewById(R.id.functionLabel);
	}

	@Override
	public void updateContent () {
		super.updateContent();
		if ( this.getPart().getCastedModel().getTypeID() == ELocationType.UNKNOWN ) {
			locationRegion.setText(this.getPart().getTLocationRegion());
		} else {
			locationRegion.setText(this.getPart().getTLocationRegionConstellation());
		}
		locationSystem.setText(this.getPart().getTLocationSystem());
		if ( this.getPart().getCastedModel().getTypeID() != ELocationType.UNKNOWN )
			locationIdentifier.setText(this.getPart().getTLocationID());
		locationStation.setText(this.getPart().getTLocationStation());
		childCount.setText(this.getPart().getTLocationContentCount());

		// The value and volume depends on a setting.
		double itemsValueISK = this.getPart().getItemsValoration();
		if ( itemsValueISK > 0.0 ) {
			locationItemsValue.setText(this.generatePriceString(itemsValueISK, true, false));
			locationItemsValueLabel.setText(this.generatePriceSuffix(itemsValueISK));
		} else {
			locationItemsValue.setText("-");
		}
		double itemsVolume = this.getPart().getItemsVolume();
		if ( itemsVolume > 0.0 ) {
			locationItemsVolume.setText(NeoComRender.qtyFormatter.format(itemsVolume));
		} else {
			locationItemsVolume.setText("-");
		}

		//		// Control the ROLE block
		//		ArrayList<Property> roles = this.getPart().accessLocationFunction();
		//		function.setVisibility(View.GONE);
		//		functionLabel.setVisibility(View.GONE);
		//		if ( (null != roles) && (roles.size() > 0) ) {
		//			String functionName = "";
		//			for (Property role : roles) {
		//				functionName = functionName + " " + role.getStringValue();
		//			}
		//			function.setVisibility(View.VISIBLE);
		//			functionLabel.setVisibility(View.VISIBLE);
		//			function.setText(functionName);
		//		}
		this.loadEveIcon((ImageView) _convertView.findViewById(R.id.nodeIcon), this.getPart().searchStationType(), true);
	}

	@Override
	protected void createView () {
		_convertView = inflateView(R.layout.location4assets);
		_convertView.setTag(this);
	}
}

// - UNUSED CODE ............................................................................................
