//  PROJECT:     NeoCom.Android (NEOC.A)
//  AUTHORS:     Adam Antinoo - adamantinoo.git@gmail.com
//  COPYRIGHT:   (c) 2013-2018 by Dimensinfin Industries, all rights reserved.
//  ENVIRONMENT: Android API22.
//  DESCRIPTION: Android Application related to the Eve Online game. The purpose is to download and organize
//               the game data to help capsuleers organize and prioritize activities. The strong points are
//               help at the Industry level tracking and calculating costs and benefits. Also the market
//               information update service will help to identify best prices and locations.
//               Planetary Interaction and Ship fittings are point under development.
//               ESI authorization is a new addition that will give continuity and allow download game data
//               from the new CCP data services.
//               This is the Android application version but shares libraries and code with other application
//               designed for Spring Boot Angular 4 platform.
//               The model management is shown using a generic Model View Controller that allows make the
//               rendering of the model data similar on all the platforms used.
package org.dimensinfin.eveonline.neocom.part;

import android.app.Activity;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.model.Ship;
import org.dimensinfin.eveonline.neocom.part.core.NeoComExpandablePart;
import org.dimensinfin.eveonline.neocom.render.NewBaseExpandableRender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// - CLASS IMPLEMENTATION ...................................................................................
public class ShipPart extends NeoComExpandablePart /*implements OnClickListener, IMenuActionTarget*/ {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static final long serialVersionUID = -8714502444756843667L;
	private static Logger logger = LoggerFactory.getLogger("ShipPart");

	// - F I E L D - S E C T I O N ............................................................................
	/** This field if to remove the click after a long click. */
	private boolean clickOverride = false;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public ShipPart (final Ship node) {
		super(node);
		// Set the expanded state by default
		//		this.getCastedModel().compress(false);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public Ship getCastedModel () {
		return (Ship) super.getModel();
	}

	public long getModelID () {
		return getCastedModel().getAssetId();
	}

	//	/**
	//	 * The goal of this method is to return the list of Parts on the children list that should be visible and
	//	 * that collaborates to the ListView population. The default behavior for this method is to check if the
	//	 * Model behind the Part is expanded, in that case the children have the opportunity to be added to the
	//	 * visible list.<br>
	//	 * If the Region is not expanded the list of elements collaborated should be:
	//	 * <ul>
	//	 * <li>Region</li>
	//	 * </ul>
	//	 * If the Region is expanded and has a number of contents the collaboration changes to add the Separators:
	//	 * <ul>
	//	 * <li>Separator Red</li>
	//	 * <li>Region</li>
	//	 * <li>Location...</li>
	//	 * <li>Separator Red</li>
	//	 * </ul>
	//	 */
	//	@Override
	//	public ArrayList<IPart> collaborate2View() {
	//		ArrayList<IPart> result = new ArrayList<IPart>();
	//		// Check if the Part is visible and collaborates to the LiastView.
	//		if (this.isVisible()) {
	//			// First we have to check if we have contents or if the part should be shown altogether.
	//			int contentCount = this.getCastedModel().getContentCount();
	//			// If the node is expanded then give the children the opportunity to also be added.
	//			if (this.isExpanded()) {
	//				if (contentCount < 1) {
	//					if (this.isRenderWhenEmpty()) {
	//						// The region is surrounded with the Red Separator at the beginning and at the end.
	//						result.add(new SeparatorPart(new Separator().setType(ESeparatorType.LINE_GREEN))
	//								.setRenderMode(this.getRenderMode()));
	//						result.add(this);
	//						// This region is empty of contents. Add the Empty Separator and complete.
	//						result.add(new SeparatorPart(new Separator().setType(ESeparatorType.EMPTY_SIGNAL))
	//								.setRenderMode(this.getRenderMode()));
	//						result.add(new SeparatorPart(new Separator().setType(ESeparatorType.LINE_GREEN))
	//								.setRenderMode(this.getRenderMode()));
	//					}
	//				} else {
	//					// Generate the list of Parts that should be collaborated to the ListView.
	//					// The region is surrounded with the Red Separator at the beginning and at the end.
	//					result.add(new SeparatorPart(new Separator().setType(ESeparatorType.LINE_GREEN))
	//							.setRenderMode(this.getRenderMode()));
	//					result.add(this);
	//					// --- This is the section where we get the Contents and then we apply the customized Part policies.
	//					Vector<IPart> childParts = this.runPolicies(this.getChildren());
	//					// --- End of policies
	//					for (IPart part : childParts) {
	//						// For each of the children that are registered to this part and that have a model recursively search for collaborations.
	//						ArrayList<IPart> collaboration = part.collaborate2View();
	//						if (!part.isNewImplemented()) {
	//							result.add(part);
	//						}
	//						result.addAll(collaboration);
	//					}
	//					result.add(new SeparatorPart(new Separator().setType(ESeparatorType.LINE_GREEN))
	//							.setRenderMode(this.getRenderMode()));
	//				}
	//			} else {
	//				result.add(this);
	//			}
	//		}
	//		return result;
	//	}

	public String get_shipClassGroup () {
		return this.getCastedModel().getName() + " - " + this.getCastedModel().getGroupName();
	}

	public Spanned get_assetLocation () {
		return this.colorFormatLocation(this.getCastedModel().getLocation());
	}

	public String getTAssetName () {
		return this.getCastedModel().getName();
	}

	/**
	 * The ship name is the user given name or in case it is not set we resort to the asset id as a
	 * differentiation item and ordering mechanism. Maybe I should use the ship category as a prefix to order
	 * the ships by categories but currently I will use only the asset identification.
	 */
	public String getName () {
		return this.get_shipClassGroup();
	}

	//	@Override
	//	public void onClick(final View view) {
	//		if (!clickOverride) {
	//			// Clean the view to force an update.
	//			this.invalidate();
	//			this.toggleExpanded();
	//			this.fireStructureChange(SystemWideConstants.events.EVENTSTRUCTURE_ACTIONEXPANDCOLLAPSE.name(), this, this);
	//			clickOverride = false;
	//		}
	//	}

	//	/**
	//	 * This is the method called when the user select one item on the context menu.
	//	 */
	//	public boolean onContextItemSelected(final MenuItem item) {
	//		ShipPart.logger.info(">> [ShipPart.onContextItemSelected]");
	//		final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
	//		final int menuItemIndex = item.getItemId();
	//		// Process the command depending on the menu and the item selected
	//		switch (menuItemIndex) {
	//			case R.id.addshipasfitting:
	//				// Add this ship as a fitting.
	//				Fitting fit = new Fitting(this.getPilot().getAssetsManager());
	//				// Copy ship items to the fitting.
	//				Ship ship = this.getCastedModel();
	//				// Copy ship items to the fitting.
	//				fit.addHull(ship.getTypeID());
	//				for (NeoComAsset module : ship.getModules()) {
	//					fit.fitModule(module.getTypeID());
	//				}
	//				for (NeoComAsset module : ship.getRigs()) {
	//					fit.fitModule(module.getTypeID());
	//				}
	//				for (NeoComAsset module : ship.getDrones()) {
	//					fit.fitModule(module.getTypeID());
	//				}
	//				for (NeoComAsset module : ship.getCargo()) {
	//					fit.fitModule(module.getTypeID());
	//				}
	//				// Activate the fitting Activity with this fit as reference. And the pilot
	//				String label = this.getCastedModel().getUserLabel();
	//				if (null == label) {
	//					label = this.getCastedModel().getItemName();
	//				}
	//				fit.setName(label);
	//				NeoComModelStore.getSingleton().addFitting(fit, label);
	//				NeoComModelStore.getSingleton().save();
	//
	//				// Open the Fitting Activity
	//				final Intent intent = new Intent(this.getActivity(), FittingActivity.class);
	//				intent.putExtra(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name(), this.getPilot().getCharacterID());
	//				intent.putExtra(AppWideConstants.EExtras.EXTRA_FITTINGID.name(), label);
	//				//				this.getActivity().startActivity(intent);
	//				break;
	//
	//			default:
	//				break;
	//		}
	//		this.invalidate();
	//		// REFACTOR The event fires a EVENTSTRUCTURE_NEEDSREFRESH that is not
	//		// processed by the different event managers.
	//		this.fireStructureChange(AppWideConstants.events.EVENTSTRUCTURE_RECALCULATE, this, this);
	//		ShipPart.logger.info("<< [ShipPart.onContextItemSelected]");
	//		return true;
	//	}

	//	/**
	//	 * If the user stays for some time on this view element then we should open a context menu that allows to
	//	 * select an action to add this ship as a fitting blueprint to be used in Industry jobs. The contextual menu
	//	 * allows for a better control of the interaction because allows to create a selection menu or a dialog.
	//	 */
	//	public void onCreateContextMenu(final ContextMenu menu, final View view, final ContextMenuInfo menuInfo) {
	//		// Clear click detection.
	//		clickOverride = false;
	//		// Show the menu to add this ship configuration as a Fitting.
	//		this.getActivity().getMenuInflater().inflate(R.menu.addshipasfitting_menu, menu);
	//	}

	@Override
	public String toString () {
		StringBuffer buffer = new StringBuffer("ShipPart [");
		buffer.append(this.getTAssetName());
		buffer.append(" ]");
		return buffer.toString();
	}

	@Override
	protected AbstractRender selectRenderer () {
		//		// Get the proper holder from the render mode.
		//		if (this.getRenderMode() == AppWideConstants.fragment.FRAGMENT_PILOTINFO_SHIPS)
		//			return new Ship4PilotInfoHolder(this, _activity);
		//		if (this.getRenderMode() == AppWideConstants.fragment.FRAGMENT_ASSETSARESHIPS)
		//			return new Ship4AssetRender(this, _activity);
		//		if (this.getRenderMode() == AppWideConstants.rendermodes.RENDER_SHIP4ASSETSBYLOCATION)
		//			return new Ship4AssetRender(this, _activity);
		//		if (this.getRenderMode() == EShipsVariants.SHIPS_BYLOCATION.hashCode())
		//			return new Ship4AssetRender(this, _activity);
		return new Ship4AssetRender(this, _activity);
	}

	// - CLASS IMPLEMENTATION ...................................................................................
	public static class Ship4AssetRender extends NewBaseExpandableRender {
		// - S T A T I C - S E C T I O N ..........................................................................

		// - F I E L D - S E C T I O N ............................................................................
		public TextView shipType = null;
		public TextView shipName = null;
		public TextView shipLocation = null;

		// - C O N S T R U C T O R - S E C T I O N ................................................................
		public Ship4AssetRender (final ShipPart target, final Activity context) {
			super(target, context);
		}

		// - M E T H O D - S E C T I O N ..........................................................................
		@Override
		public ShipPart getPart () {
			return (ShipPart) super.getPart();
		}

		@Override
		public void initializeViews () {
			super.initializeViews();
			shipType = (TextView) _convertView.findViewById(R.id.shipType);
			shipName = (TextView) _convertView.findViewById(R.id.shipName);
			shipLocation = (TextView) _convertView.findViewById(R.id.shipLocation);
		}

		@Override
		public void updateContent () {
			super.updateContent();
			shipType.setText(getPart().get_shipClassGroup());
			String name = getPart().getCastedModel().getUserLabel();
			if ( null == name )
				shipName.setVisibility(View.GONE);
			else {
				shipName.setVisibility(View.VISIBLE);
				shipName.setText(name);
			}
			shipLocation.setText(getPart().get_assetLocation());
			loadEveIcon((ImageView) _convertView.findViewById(R.id.nodeIcon), getPart().getCastedModel().getTypeId());
		}

		@Override
		protected void createView () {
			_convertView = inflateView(R.layout.ship4listbylocation);
			_convertView.setTag(this);
		}
	}
}

// - UNUSED CODE ............................................................................................
