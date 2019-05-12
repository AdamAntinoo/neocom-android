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
import android.graphics.Typeface;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.dimensinfin.android.mvc.constants.SystemWideConstants;
import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.android.mvc.interfaces.IMenuActionTarget;
import org.dimensinfin.android.mvc.interfaces.IPart;
import org.dimensinfin.core.model.Separator;
import org.dimensinfin.eveonline.neocom.R;
//import org.dimensinfin.eveonline.neocom.entity.Credential;
//import org.dimensinfin.eveonline.neocom.entity.Property;
import org.dimensinfin.eveonline.neocom.sharedcode.dataservice.AndroidGlobalDataManager;
import org.dimensinfin.eveonline.neocom.entities.Credential;
import org.dimensinfin.eveonline.neocom.entities.Property;
import org.dimensinfin.eveonline.neocom.exception.NeoComRegisteredException;
import org.dimensinfin.eveonline.neocom.interfaces.ICredentialActivity;
import org.dimensinfin.eveonline.neocom.model.EveLocation;
import org.dimensinfin.eveonline.neocom.model.LocationFacetedAssetsContainer;
import org.dimensinfin.eveonline.neocom.model.PilotV2;
import org.dimensinfin.eveonline.neocom.part.core.NeoComAbstractExpandablePart;
import org.dimensinfin.eveonline.neocom.part.core.NeoComAbstractPart;
import org.dimensinfin.eveonline.neocom.render.ANodeBaseDecoratedRender;
import org.dimensinfin.eveonline.neocom.ui.CustomTypefaceSpan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class Location4AssetsPart extends NeoComAbstractExpandablePart implements IMenuActionTarget {
	// - S T A T I C - S E C T I O N ..........................................................................
	protected static Comparator<IPart> assetNameAscendingComparator = ( IPart part1, IPart part2 ) -> {
		String partName1 = ((AssetPart) part1).getTAssetName().toUpperCase();
		String partName2 = ((AssetPart) part2).getTAssetName().toUpperCase();

		//ascending order
		return partName1.compareTo(partName2);
	};
	protected static Comparator<IPart> assetNameDescendingComparator = ( IPart part1, IPart part2 ) -> {
		String partName1 = ((AssetPart) part1).getTAssetName().toUpperCase();
		String partName2 = ((AssetPart) part2).getTAssetName().toUpperCase();

		//ascending order
		return partName2.compareTo(partName1);
	};

	// - F I E L D - S E C T I O N ............................................................................
	private PilotV2 pilot = null;
	private List<Property> roles = new ArrayList<>();

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public Location4AssetsPart( final LocationFacetedAssetsContainer model ) {
		super(model);
		this.setThemeColor(Separator.ESeparatorType.LINE_ORANGE);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public LocationFacetedAssetsContainer getCastedModel() {
		return (LocationFacetedAssetsContainer) this.getModel();
	}

	// --- M O D E L   D A T A   T R A N S F O R M A T I O N
	public Spanned getTStationName() {
		StringBuffer htmlLocation = new StringBuffer();
		double security = this.getCastedModel().getSecurityValue();
		htmlLocation.append(NeoComAbstractPart.generateSecurityColor(security, NeoComAbstractPart.securityFormatter.format(security)));
		htmlLocation.append(" ").append(this.getCastedModel().getStation());
		return Html.fromHtml(htmlLocation.toString());
	}

	public String getTContentsCount() {
		int locationAssets = getCastedModel().getContentSize();
		String countString = null;
		if (locationAssets > 1) {
			countString = NeoComAbstractPart.qtyFormatter.format(locationAssets) + " items";
		} else {
			countString = NeoComAbstractPart.qtyFormatter.format(locationAssets) + " item";
		}
		return countString;
	}

	public String getTContentsValue() {
		return NeoComAbstractPart.generatePriceString(getCastedModel().getTotalValue(), true, true);
	}

	public String getTContentsVolume() {
		return NeoComAbstractPart.qtyFormatter.format(getCastedModel().getTotalVolume()) + " m3";
	}

	// --- I A N D R O I D P A R T   I N T E R F A C E
	@Override
	public long getModelId() {
		return this.getCastedModel().getId();
	}

	@Override
	public AbstractRender selectRenderer() {
		return new Location4MiningRender(this, getActivity());
	}

	@Override
	public List<IPart> runPolicies( List<IPart> targets ) {
		// Sort contents alphabetically by the asset name.
		Collections.sort(targets, assetNameAscendingComparator);
		return super.runPolicies(targets);
	}

	@Override
	public boolean runDependencies() {
		// Collect the location roles references.
		try {
			if (this.getActivity() instanceof ICredentialActivity) {
				final Credential credential = ((ICredentialActivity) this.getActivity()).getCredential();
				if (null != credential) {
					pilot = AndroidGlobalDataManager.fastRequestPilotV2(credential);
					if (null != pilot) {
						roles = pilot.getLocationRoles();
					} else Toast.makeText(getActivity(), "EX: Pilot not located!!", Toast.LENGTH_SHORT).show();
				} else Toast.makeText(getActivity(), "EX: Credential not located!!", Toast.LENGTH_SHORT).show();
			} else Toast.makeText(getActivity(), "EX: Activity not Credential aware!!", Toast.LENGTH_SHORT).show();
		} catch (NeoComRegisteredException neoe) {
			neoe.printStackTrace();
		}
		return true;
	}

	// --- I M E N U A C T I O N T A R G E T   I N T E R F A C E
	@Override
	public boolean onContextItemSelected( MenuItem item ) {
		logger.info(">> [Location4MiningPart.onContextItemSelected]");
		try {
			final int menuItemIndex = item.getItemId();
			final int itemSelected = item.getItemId();
			final CharSequence menuItemTitle = item.getTitle();
			final EveLocation theSelectedLocation = getCastedModel();
			if (null != pilot) {
				if (null != roles) {
					if (menuItemIndex == R.id.context_locationrole_clear)
						pilot.clearLocationRoles(theSelectedLocation);
					else {
						final Property role = pilot.addLocationRole(theSelectedLocation, menuItemTitle.toString());
						roles.add(role);
					}
				}
			}
			invalidate();
			fireStructureChange(SystemWideConstants.events.EVENTCONTENTS_ACTIONMODIFYDATA.name(), this, this);
		} finally {
			logger.info("<< [Location4MiningPart.onContextItemSelected]");
			return true;
		}
	}

	@Override
	public void onCreateContextMenu( ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo ) {
		logger.info(">> [Location4MiningPart.onCreateContextMenu]");
		// Initialize the contextual menu each time.
		MenuInflater inflater = new MenuInflater(AndroidGlobalDataManager.getAppContext());
		inflater.inflate(R.menu.context_location4assets, menu);

		// Change the style for the application menu. This changes the text font and the background.
		// - B A C K G R O U N D - By intercepting the creation of the manu view.
		//		this.getActivity().getLayoutInflater().setFactory(( String name, Context context, AttributeSet attrs ) -> {
		//			if (name.startsWith("com.android.internal.view.menu.ListMenuItemView")) {
		//				try {
		//					// Create the view and replace the desired attributes before releasing.
		//					final View menuView = this.getActivity().getLayoutInflater().createView(name, null, attrs);
		//					new Handler().post(() -> {
		//						// Set the background drawable
		//						menuView.setBackgroundResource(R.drawable.blacktraslucent80);
		//					});
		//					return menuView;
		//				} catch (InflateException e) {
		//				} catch (ClassNotFoundException e) {
		//				}
		//			}
		//			return null;
		//		});
		// - F O N T - By replacing this property on each menu item found.
		for (int i = 0; i < menu.size(); i++) {
			MenuItem mi = menu.getItem(i);

			//for applying a font to subMenu ...
			SubMenu subMenu = mi.getSubMenu();
			if (subMenu != null && subMenu.size() > 0) {
				for (int j = 0; j < subMenu.size(); j++) {
					MenuItem subMenuItem = subMenu.getItem(j);
					applyFontToMenuItem(subMenuItem);
				}
			}
			// If the role is active then use the Bold font. Otherwise the standard one.
			final String menuTitle = mi.getTitle().toString();
			if (roleActive(menuTitle))
				applyBoldFontToMenuItem(mi);
			else
				applyFontToMenuItem(mi);
		}
		logger.info("<< [Location4MiningPart.onCreateContextMenu]");
	}

	protected boolean roleActive( final String roleName ) {
		for (Property prop : roles) {
			if (prop.getStringValue().equalsIgnoreCase(roleName)) return true;
		}
		return false;
	}

	protected void applyBoldFontToMenuItem( MenuItem mi ) {
		Typeface font = Typeface.createFromAsset(this.getActivity().getAssets(), "fonts/Proxima Nova Bold.ttf");
		SpannableString mNewTitle = new SpannableString(mi.getTitle());
		mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		mi.setTitle(mNewTitle);
	}

	protected void applyFontToMenuItem( MenuItem mi ) {
		Typeface font = Typeface.createFromAsset(this.getActivity().getAssets(), "fonts/Proxima Nova Thin.ttf");
		SpannableString mNewTitle = new SpannableString(mi.getTitle());
		mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		mi.setTitle(mNewTitle);
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("Location4MiningPart [ ");
		buffer.append("name: ").append(0);
		buffer.append("]");
		buffer.append("->").append(super.toString());
		return buffer.toString();
	}

	// - CLASS IMPLEMENTATION ...................................................................................
	public static class Location4MiningRender extends ANodeBaseDecoratedRender {
		// - F I E L D - S E C T I O N ............................................................................
		private TextView locationRegion = null;
		private TextView locationSystem = null;
		private TextView locationIdentifier = null;
		private TextView locationStation = null;
		private TextView contentsCount = null;
		private TextView locationItemsValue = null;
		private TextView locationItemsVolume = null;


		// - C O N S T R U C T O R - S E C T I O N ................................................................
		public Location4MiningRender( NeoComAbstractExpandablePart newPart, Activity context ) {
			super(newPart, context);
		}

		// - M E T H O D - S E C T I O N ..........................................................................
		@Override
		public Location4AssetsPart getPart() {
			return (Location4AssetsPart) super.getPart();
		}

		// --- A B S T R A C T R E N D E R   O V E R R I D E S
		@Override
		public void initializeViews() {
			super.initializeViews();
			locationRegion = (TextView) _convertView.findViewById(R.id.locationRegion);
			locationSystem = (TextView) _convertView.findViewById(R.id.locationSystem);
			locationIdentifier = (TextView) _convertView.findViewById(R.id.locationIdentifier);
			locationStation = (TextView) _convertView.findViewById(R.id.locationStation);
			contentsCount = (TextView) _convertView.findViewById(R.id.contentsCount);
			locationItemsValue = (TextView) _convertView.findViewById(R.id.locationItemsValue);
			locationItemsVolume = (TextView) _convertView.findViewById(R.id.locationItemsVolume);
		}

		@Override
		public void updateContent() {
			super.updateContent();
			final String link = getPart().getCastedModel().getUrlLocationIcon();
			if ((null != link) && (null != nodeIcon))
				Picasso.get().load(link).into(nodeIcon);
			locationRegion.setText(this.getPart().getCastedModel().getRegion());
			locationSystem.setText(this.getPart().getCastedModel().getSystem());
			locationIdentifier.setText("[" + this.getPart().getCastedModel().getId() + "]");
			locationStation.setText(this.getPart().getTStationName());
			contentsCount.setText(this.getPart().getTContentsCount());
			locationItemsValue.setText(this.getPart().getTContentsValue());
			locationItemsVolume.setText(this.getPart().getTContentsVolume());
		}

		@Override
		public int accessLayoutReference() {
			return R.layout.location4mining;
		}
	}
}
// - UNUSED CODE ............................................................................................
//[01]
