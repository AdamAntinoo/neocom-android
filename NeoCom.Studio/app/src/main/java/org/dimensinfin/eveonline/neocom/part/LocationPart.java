//	PROJECT:        EVEIndustrialist (EVEI)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2014 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API11.
//	DESCRIPTION:		Application helper for Eve Online Industrialists. Will help on Industry and Manufacture.

package org.dimensinfin.eveonline.neocom.part;

// - IMPORT SECTION .........................................................................................
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Logger;

import org.dimensinfin.android.mvc.interfaces.INamedPart;
import org.dimensinfin.android.mvc.interfaces.IPart;
import org.dimensinfin.eveonline.neocom.NeoComApp;
import org.dimensinfin.eveonline.neocom.connector.NeoComAppConnector;
import org.dimensinfin.eveonline.neocom.constant.AppWideConstants;
import org.dimensinfin.eveonline.neocom.constant.ModelWideConstants;
import org.dimensinfin.eveonline.neocom.model.EveItem;
import org.dimensinfin.eveonline.neocom.model.EveLocation;
import org.dimensinfin.eveonline.neocom.model.NeoComAsset;
import org.dimensinfin.eveonline.neocom.refactoring.AExtendedPart;

import android.text.Html;
import android.text.Spanned;
// - CLASS IMPLEMENTATION ...................................................................................

//- CLASS IMPLEMENTATION ...................................................................................
public abstract class LocationPart extends AExtendedPart implements INamedPart {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static final long																	serialVersionUID	= 6823835480477273409L;
	protected static Logger																		logger						= Logger.getLogger("LocationPart");

	// - F I E L D - S E C T I O N ............................................................................
	@SuppressWarnings("rawtypes")
	private final HashMap<Long, HashMap<Integer, AssetPart>>	stackList					= new HashMap();
	protected double																					itemsValueISK			= 0.0;
	protected double																					itemsVolume				= 0.0;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public LocationPart(final EveLocation location) {
		super(location);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public ArrayList accessLocationFunction() {
		return this.getPilot().getLocationRoles(this.getCastedModel().getID(), "-NO-FUNCTION-");
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
	//						result.add(new SeparatorPart(new Separator().setType(ESeparatorType.LINE_ORANGE))
	//								.setRenderMode(this.getRenderMode()));
	//						result.add(this);
	//						// This region is empty of contents. Add the Empty Separator and complete.
	//						result.add(new SeparatorPart(new Separator().setType(ESeparatorType.EMPTY_SIGNAL))
	//								.setRenderMode(this.getRenderMode()));
	//						result.add(new SeparatorPart(new Separator().setType(ESeparatorType.LINE_ORANGE))
	//								.setRenderMode(this.getRenderMode()));
	//					}
	//				} else {
	//					// Generate the list of Parts that should be collaborated to the ListView.
	//					// The region is surrounded with the Red Separator at the beginning and at the end.
	//					result.add(new SeparatorPart(new Separator().setType(ESeparatorType.LINE_ORANGE))
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
	//					result.add(new SeparatorPart(new Separator().setType(ESeparatorType.LINE_ORANGE))
	//							.setRenderMode(this.getRenderMode()));
	//				}
	//			} else {
	//				result.add(this);
	//			}
	//		}
	//		return result;
	//	}

	public long get_locationID() {
		return this.getCastedModel().getID();
	}

	public Spanned get_locationRegion() {
		double security = this.getCastedModel().getSecurityValue();
		return Html.fromHtml(this.generateSecurityColor(security,
				AppWideConstants.FLOW_ARROW_RIGHT + this.getCastedModel().getConstellation()));
	}

	public Spanned get_locationRegionConstellation() {
		double security = this.getCastedModel().getSecurityValue();
		return Html.fromHtml(this.generateSecurityColor(security, this.getCastedModel().getRegion()
				+ AppWideConstants.FLOW_ARROW_RIGHT + this.getCastedModel().getConstellation()));
	}

	public Spanned get_locationStation() {
		StringBuffer htmlLocation = new StringBuffer();
		double security = this.getCastedModel().getSecurityValue();
		htmlLocation.append(this.generateSecurityColor(security, NeoComAbstractPart.securityFormatter.format(security)));
		htmlLocation.append(" ").append(this.getCastedModel().getStation());
		return Html.fromHtml(htmlLocation.toString());
	}

	public String get_locationSystem() {
		return this.getCastedModel().getSystem();
	}

	@Override
	public EveLocation getCastedModel() {
		return (EveLocation) this.getModel();
	}

	@Override
	public long getModelID() {
		return ((EveLocation) this.getModel()).getID();
	}

	public String getName() {
		return this.getCastedModel().getName();
	}

	/**
	 * Returns the number of model part children that are the blueprints at that location. If the case on a same
	 * station there are more than one container they will be represented as different locations.
	 *
	 * @return
	 */
	public String getTLocationContentCount() {
		int locationAssets = this.getChildren().size();
		String countString = null;
		if (locationAssets > 1) {
			countString = NeoComAbstractPart.qtyFormatter.format(locationAssets) + " Stacks";
		} else {
			countString = NeoComAbstractPart.qtyFormatter.format(locationAssets) + " Stack";
		}
		return countString;
	}

	//	@Override
	//	public ArrayList<AbstractAndroidPart> getPartChildren() {
	//		ArrayList<AbstractAndroidPart> result = new ArrayList<AbstractAndroidPart>();
	//		Vector<AbstractPropertyChanger> ch = this.getChildren();
	//		Collections.sort(ch, EVEDroidApp.createComparator(AppWideConstants.comparators.COMPARATOR_NAME));
	//		Collections.sort(ch, EVEDroidApp.createComparator(AppWideConstants.comparators.COMPARATOR_ITEM_TYPE));
	//
	//		for (AbstractPropertyChanger node : ch) {
	//			// Convert the node to a part.
	//			AbstractAndroidPart part = (AbstractAndroidPart) node;
	//			result.add(part);
	//			// Check if the node is expanded. Then add its children.
	//			if (part.isExpanded()) {
	//				ArrayList<AbstractAndroidPart> grand = part.getPartChildren();
	//				result.addAll(grand);
	//				// Add a separator.
	//				result.add(new TerminatorPart(new Separator("")));
	//			}
	//		}
	//		return result;
	//	}
	/**
	 * The policies for locations are two sorts. The first one by name the second by item type.
	 */
	@Override
	public Vector<IPart> runPolicies(final Vector<IPart> targets) {
		Collections.sort(targets, NeoComApp.createPartComparator(AppWideConstants.comparators.COMPARATOR_NAME));
		Collections.sort(targets, NeoComApp.createPartComparator(AppWideConstants.comparators.COMPARATOR_ITEM_TYPE));
		return targets;
	}

	public int searchStationType() {
		return NeoComAppConnector.getSingleton().getCCPDBConnector()
				.searchStationType(this.getCastedModel().getStationID());
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("LocationPart [");
		buffer.append(this.getCastedModel());
		buffer.append(" ]");
		return buffer.toString();
	}

	protected void calculateValue(final NeoComAsset asset, final AssetPart apart) {
		// Skip blueprints from the value calculations
		if (null != asset) {
			EveItem item = asset.getItem();
			if (null != item) {
				String category = item.getCategory();
				String group = item.getGroupName();
				if (null != category) if (!category.equalsIgnoreCase(ModelWideConstants.eveglobal.Blueprint)) {
					// Add the value and volume of the stack to the global result.
					long quantity = apart.getCastedModel().getQuantity();
					double price = apart.getCastedModel().getItem().getHighestBuyerPrice().getPrice();
					itemsValueISK += price * quantity;
					// Add volume but if it is an assembled ship or assembled container
					if ((group.equalsIgnoreCase("Secure Cargo Container")) || (category.equalsIgnoreCase("Ship"))) {
						itemsVolume += 0;
					} else {
						itemsVolume += quantity * apart.getCastedModel().getItem().getVolume();
					}
				}
			}
		}
	}

	protected void checkAssetStacking(/* final LocationPart target, */final AssetPart apart) {
		// Locate the stack if exists.
		HashMap<Integer, AssetPart> container = stackList.get(this.getCastedModel().getID());
		int type = apart.getCastedModel().getTypeID();
		if (null != container) {
			AssetPart stack = container.get(type);
			if (null != stack) {
				//	if (stack instanceof AssetPart) {
				int count = stack.getCastedModel().getQuantity();
				stack.getCastedModel().setQuantity(count + 1);
				return;
				//	}
				// Do nothing because we do not know how to add to Assets.
			} else {
				// Add a new stack for this type to the current container.
				container.put(type, apart);
				this.addChild(apart);
			}
		} else {
			// There is no container also with this stack.
			container = new HashMap<Integer, AssetPart>();
			container.put(type, apart);
			this.addChild(apart);
			stackList.put(this.getCastedModel().getID(), container);
		}
	}
}
// - UNUSED CODE ............................................................................................
