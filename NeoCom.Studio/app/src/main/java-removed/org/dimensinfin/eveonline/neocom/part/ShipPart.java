//	PROJECT:        NeoCom.Android (NEOC.A)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2016 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API16.
//	DESCRIPTION:		Application to get access to CCP api information and help manage industrial activities
//									for characters and corporations at Eve Online. The set is composed of some projects
//									with implementation for Android and for an AngularJS web interface based on REST
//									services on Sprint Boot Cloud.
package org.dimensinfin.eveonline.neocom.part;

import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Logger;

import org.dimensinfin.android.model.Separator;
import org.dimensinfin.android.model.Separator.ESeparatorType;
import org.dimensinfin.android.mvc.constants.SystemWideConstants;
import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.android.mvc.interfaces.IMenuActionTarget;
import org.dimensinfin.android.mvc.interfaces.IPart;
import org.dimensinfin.android.mvc.part.SeparatorPart;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.activity.FittingActivity;
import org.dimensinfin.eveonline.neocom.activity.ShipDirectorActivity.EShipsVariants;
import org.dimensinfin.eveonline.neocom.constant.AppWideConstants;
import org.dimensinfin.eveonline.neocom.model.Fitting;
import org.dimensinfin.eveonline.neocom.model.NeoComAsset;
import org.dimensinfin.eveonline.neocom.model.Ship;
import org.dimensinfin.eveonline.neocom.render.Ship4AssetHolder;
import org.dimensinfin.eveonline.neocom.render.Ship4PilotInfoHolder;
import org.dimensinfin.eveonline.neocom.storage.NeoComModelStore;

import android.content.Intent;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;

// - CLASS IMPLEMENTATION ...................................................................................
public class ShipPart extends AssetPart implements OnClickListener, IMenuActionTarget {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static final long	serialVersionUID	= -8714502444756843667L;
	private static Logger			logger						= Logger.getLogger("ShipPart");

	// - F I E L D - S E C T I O N ............................................................................
	/** This field if to remove the click after a long click. */
	private boolean						clickOverride			= false;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public ShipPart(final NeoComAsset node) {
		super(node);
		// Set the expanded state by default
		this.getCastedModel().setExpanded(false);
	}

	/**
	 * The goal of this method is to return the list of Parts on the children list that should be visible and
	 * that collaborates to the ListView population. The default behavior for this method is to check if the
	 * Model behind the Part is expanded, in that case the children have the opportunity to be added to the
	 * visible list.<br>
	 * If the Region is not expanded the list of elements collaborated should be:
	 * <ul>
	 * <li>Region</li>
	 * </ul>
	 * If the Region is expanded and has a number of contents the collaboration changes to add the Separators:
	 * <ul>
	 * <li>Separator Red</li>
	 * <li>Region</li>
	 * <li>Location...</li>
	 * <li>Separator Red</li>
	 * </ul>
	 */
	@Override
	public ArrayList<IPart> collaborate2View() {
		ArrayList<IPart> result = new ArrayList<IPart>();
		// Check if the Part is visible and collaborates to the LiastView.
		if (this.isVisible()) {
			// First we have to check if we have contents or if the part should be shown altogether.
			int contentCount = this.getCastedModel().getContentCount();
			// If the node is expanded then give the children the opportunity to also be added.
			if (this.isExpanded()) {
				if (contentCount < 1) {
					if (this.isRenderWhenEmpty()) {
						// The region is surrounded with the Red Separator at the beginning and at the end.
						result.add(new SeparatorPart(new Separator().setType(ESeparatorType.LINE_GREEN))
								.setRenderMode(this.getRenderMode()));
						result.add(this);
						// This region is empty of contents. Add the Empty Separator and complete.
						result.add(new SeparatorPart(new Separator().setType(ESeparatorType.EMPTY_SIGNAL))
								.setRenderMode(this.getRenderMode()));
						result.add(new SeparatorPart(new Separator().setType(ESeparatorType.LINE_GREEN))
								.setRenderMode(this.getRenderMode()));
					}
				} else {
					// Generate the list of Parts that should be collaborated to the ListView.
					// The region is surrounded with the Red Separator at the beginning and at the end.
					result.add(new SeparatorPart(new Separator().setType(ESeparatorType.LINE_GREEN))
							.setRenderMode(this.getRenderMode()));
					result.add(this);
					// --- This is the section where we get the Contents and then we apply the customized Part policies.
					Vector<IPart> childParts = this.runPolicies(this.getChildren());
					// --- End of policies
					for (IPart part : childParts) {
						// For each of the children that are registered to this part and that have a model recursively search for collaborations.
						ArrayList<IPart> collaboration = part.collaborate2View();
						if (!part.isNewImplemented()) {
							result.add(part);
						}
						result.addAll(collaboration);
					}
					result.add(new SeparatorPart(new Separator().setType(ESeparatorType.LINE_GREEN))
							.setRenderMode(this.getRenderMode()));
				}
			} else {
				result.add(this);
			}
		}
		return result;
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public String get_shipClassGroup() {
		return this.getCastedModel().getName() + " - " + this.getCastedModel().getGroupName();
	}

	@Override
	public Ship getCastedModel() {
		return (Ship) super.getModel();
	}

	/**
	 * The ship name is the user given name or in case it is not set we resort to the asset id as a
	 * differentiation item and ordering mechanism. Maybe I should use the ship category as a prefix to order
	 * the ships by categories but currently I will use only the asset identification.
	 */
	@Override
	public String getName() {
		return this.get_shipClassGroup();
	}

	@Override
	public void onClick(final View view) {
		if (!clickOverride) {
			// Clean the view to force an update.
			this.invalidate();
			this.toggleExpanded();
			this.fireStructureChange(SystemWideConstants.events.EVENTSTRUCTURE_ACTIONEXPANDCOLLAPSE.name(), this, this);
			clickOverride = false;
		}
	}

	/**
	 * This is the method called when the user select one item on the context menu.
	 */
	public boolean onContextItemSelected(final MenuItem item) {
		ShipPart.logger.info(">> [ShipPart.onContextItemSelected]");
		final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		final int menuItemIndex = item.getItemId();
		// Process the command depending on the menu and the item selected
		switch (menuItemIndex) {
			case R.id.addshipasfitting:
				// Add this ship as a fitting.
				Fitting fit = new Fitting(this.getPilot().getAssetsManager());
				// Copy ship items to the fitting.
				Ship ship = this.getCastedModel();
				// Copy ship items to the fitting.
				fit.addHull(ship.getTypeID());
				for (NeoComAsset module : ship.getModules()) {
					fit.fitModule(module.getTypeID());
				}
				for (NeoComAsset module : ship.getRigs()) {
					fit.fitModule(module.getTypeID());
				}
				for (NeoComAsset module : ship.getDrones()) {
					fit.fitModule(module.getTypeID());
				}
				for (NeoComAsset module : ship.getCargo()) {
					fit.fitModule(module.getTypeID());
				}
				// Activate the fitting Activity with this fit as reference. And the pilot
				String label = this.getCastedModel().getUserLabel();
				if (null == label) {
					label = this.getCastedModel().getItemName();
				}
				fit.setName(label);
				NeoComModelStore.getSingleton().addFitting(fit, label);
				NeoComModelStore.getSingleton().save();

				// Open the Fitting Activity
				final Intent intent = new Intent(this.getActivity(), FittingActivity.class);
				intent.putExtra(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name(), this.getPilot().getCharacterID());
				intent.putExtra(AppWideConstants.EExtras.EXTRA_FITTINGID.name(), label);
				//				this.getActivity().startActivity(intent);
				break;

			default:
				break;
		}
		this.invalidate();
		// REFACTOR The event fires a EVENTSTRUCTURE_NEEDSREFRESH that is not
		// processed by the different event managers.
		this.fireStructureChange(AppWideConstants.events.EVENTSTRUCTURE_RECALCULATE, this, this);
		ShipPart.logger.info("<< [ShipPart.onContextItemSelected]");
		return true;
	}

	/**
	 * If the user stays for some time on this view element then we should open a context menu that allows to
	 * select an action to add this ship as a fitting blueprint to be used in Industry jobs. The contextual menu
	 * allows for a better control of the interaction because allows to create a selection menu or a dialog.
	 */
	public void onCreateContextMenu(final ContextMenu menu, final View view, final ContextMenuInfo menuInfo) {
		// Clear click detection.
		clickOverride = false;
		// Show the menu to add this ship configuration as a Fitting.
		this.getActivity().getMenuInflater().inflate(R.menu.addshipasfitting_menu, menu);
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("ShipPart [");
		buffer.append(this.getTAssetName());
		buffer.append(" ]");
		return buffer.toString();
	}

	@Override
	protected AbstractRender selectRenderer() {
		// Get the proper holder from the render mode.
		if (this.getRenderMode() == AppWideConstants.fragment.FRAGMENT_PILOTINFO_SHIPS)
			return new Ship4PilotInfoHolder(this, _activity);
		if (this.getRenderMode() == AppWideConstants.fragment.FRAGMENT_ASSETSARESHIPS)
			return new Ship4AssetHolder(this, _activity);
		if (this.getRenderMode() == AppWideConstants.rendermodes.RENDER_SHIP4ASSETSBYLOCATION)
			return new Ship4AssetHolder(this, _activity);
		if (this.getRenderMode() == EShipsVariants.SHIPS_BYLOCATION.hashCode())
			return new Ship4AssetHolder(this, _activity);
		return new Ship4AssetHolder(this, _activity);
	}
}

// - UNUSED CODE ............................................................................................
