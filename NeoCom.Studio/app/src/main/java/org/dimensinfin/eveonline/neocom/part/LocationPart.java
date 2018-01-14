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

import android.text.Html;
import android.text.Spanned;

import org.dimensinfin.android.mvc.interfaces.INamedPart;
import org.dimensinfin.android.mvc.interfaces.IPart;
import org.dimensinfin.eveonline.neocom.connector.NeoComAppConnector;
import org.dimensinfin.eveonline.neocom.constant.AppWideConstants;
import org.dimensinfin.eveonline.neocom.core.PartComparatorFactory;
import org.dimensinfin.eveonline.neocom.core.PartComparatorFactory.ComparatorType;
import org.dimensinfin.eveonline.neocom.model.EveLocation;
import org.dimensinfin.eveonline.neocom.part.core.NeoComAbstractPart;
import org.dimensinfin.eveonline.neocom.part.core.NeoComExpandablePart;

import java.util.Collections;
import java.util.Vector;
import java.util.logging.Logger;

//- CLASS IMPLEMENTATION ...................................................................................
public abstract class LocationPart extends NeoComExpandablePart implements INamedPart {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static final long serialVersionUID = 6823835480477273409L;
	protected static Logger logger = Logger.getLogger("LocationPart");

	// - F I E L D - S E C T I O N ............................................................................
	//	@SuppressWarnings("rawtypes")
	//	private final Hashtable<Long, HashMap<Integer, AssetPart>> stackList = new Hashtable();
	protected double itemsValueISK = 0.0;
	protected double itemsVolume = 0.0;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public LocationPart (final EveLocation location) {
		super(location);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public EveLocation getCastedModel () {
		return (EveLocation) this.getModel();
	}

	@Override
	public long getModelID () {
		return ((EveLocation) this.getModel()).getID();
	}

	public String getName () {
		return this.getCastedModel().getName();
	}

	public Spanned getTLocationRegion () {
		double security = this.getCastedModel().getSecurityValue();
		return Html.fromHtml(this.generateSecurityColor(security, this.getCastedModel().getConstellation()));
	}

	public Spanned getTLocationRegionConstellation () {
		double security = this.getCastedModel().getSecurityValue();
		return Html.fromHtml(this.generateSecurityColor(security, this.getCastedModel().getRegion()
				+ AppWideConstants.FLOW_ARROW_RIGHT + this.getCastedModel().getConstellation()));
	}

	public String getTLocationSystem () {
		return this.getCastedModel().getSystem();
	}

	public Spanned getTLocationStation () {
		StringBuffer htmlLocation = new StringBuffer();
		double security = this.getCastedModel().getSecurityValue();
		htmlLocation.append(this.generateSecurityColor(security, NeoComAbstractPart.securityFormatter.format(security)));
		htmlLocation.append(" ").append(this.getCastedModel().getStation());
		return Html.fromHtml(htmlLocation.toString());
	}

	/**
	 * Returns the number of model part children that are the blueprints at that location. If the case on a same
	 * station there are more than one container they will be represented as different locations.
	 */
	public String getTLocationContentCount () {
		int locationAssets = this.getChildren().size();
		String countString = null;
		if ( locationAssets > 1 ) {
			countString = NeoComAbstractPart.qtyFormatter.format(locationAssets) + " Stacks";
		} else {
			countString = NeoComAbstractPart.qtyFormatter.format(locationAssets) + " Stack";
		}
		return countString;
	}

	public int searchStationType () {
		return NeoComAppConnector.getSingleton().getCCPDBConnector()
		                         .searchStationType(this.getCastedModel().getStationID());
	}

	/**
	 * The policies for locations are two sorts. The first one by name the second by item type.
	 */
	public Vector<IPart> runPolicies (final Vector<IPart> targets) {
		Collections.sort(targets, PartComparatorFactory.createPartComparator(ComparatorType.COMPARATOR_NAME));
		Collections.sort(targets, PartComparatorFactory.createPartComparator(ComparatorType.COMPARATOR_ITEM_TYPE));
		return targets;
	}


	//	public ArrayList accessLocationFunction () {
	//		return this.getPilot().getLocationRoles(this.getCastedModel().getLocationId(), "-NO-FUNCTION-");
	//	}
	//
	//	public long get_locationID () {
	//		return this.getCastedModel().getLocationId();
	//	}


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


	@Override
	public String toString () {
		StringBuffer buffer = new StringBuffer("LocationPart [");
		buffer.append(this.getCastedModel());
		buffer.append(" ]");
		return buffer.toString();
	}

	//	protected void calculateValue (final NeoComAsset asset, final AssetPart apart) {
	//		// Skip blueprints from the value calculations
	//		if ( null != asset ) {
	//			EveItem item = asset.getItem();
	//			if ( null != item ) {
	//				String category = item.getCategory();
	//				String group = item.getGroupName();
	//				if ( null != category ) if ( !category.equalsIgnoreCase(ModelWideConstants.eveglobal.Blueprint) ) {
	//					// Add the value and volume of the stack to the global result.
	//					long quantity = apart.getCastedModel().getQuantity();
	//					double price = apart.getCastedModel().getItem().getHighestBuyerPrice().getPrice();
	//					itemsValueISK += price * quantity;
	//					// Add volume but if it is an assembled ship or assembled container
	//					if ( (group.equalsIgnoreCase("Secure Cargo Container")) || (category.equalsIgnoreCase("Ship")) ) {
	//						itemsVolume += 0;
	//					} else {
	//						itemsVolume += quantity * apart.getCastedModel().getItem().getVolume();
	//					}
	//				}
	//			}
	//		}
	//	}
	//
	//	protected void checkAssetStacking (/* final LocationPart target, */final AssetPart apart) {
	//		// Locate the stack if exists.
	//		HashMap<Integer, AssetPart> container = stackList.get(this.getCastedModel().getLocationId());
	//		int type = apart.getCastedModel().getTypeID();
	//		if ( null != container ) {
	//			AssetPart stack = container.get(type);
	//			if ( null != stack ) {
	//				//	if (stack instanceof AssetPart) {
	//				int count = stack.getCastedModel().getQuantity();
	//				stack.getCastedModel().setQuantity(count + 1);
	//				return;
	//				//	}
	//				// Do nothing because we do not know how to add to Assets.
	//			} else {
	//				// Add a new stack for this type to the current container.
	//				container.put(type, apart);
	//				this.addChild(apart);
	//			}
	//		} else {
	//			// There is no container also with this stack.
	//			container = new HashMap<Integer, AssetPart>();
	//			container.put(type, apart);
	//			this.addChild(apart);
	//			stackList.put(this.getCastedModel().getLocationId(), container);
	//		}
	//	}
}
// - UNUSED CODE ............................................................................................
