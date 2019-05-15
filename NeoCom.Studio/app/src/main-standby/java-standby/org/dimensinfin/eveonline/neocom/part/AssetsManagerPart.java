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
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.android.mvc.interfaces.INamedPart;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.activity.AssetsManagerActivity;
import org.dimensinfin.eveonline.neocom.constant.AppWideConstants;
import org.dimensinfin.eveonline.neocom.interfaces.INeoComDirectorPart;
import org.dimensinfin.eveonline.neocom.manager.AssetsManager;
import org.dimensinfin.eveonline.neocom.part.core.NeoComAbstractPart;
import org.dimensinfin.eveonline.neocom.render.NeoComRender;

// - CLASS IMPLEMENTATION ...................................................................................
public class AssetsManagerPart extends NeoComAbstractPart
		implements INeoComDirectorPart, INamedPart, OnClickListener {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static final long serialVersionUID = -8992564305513190092L;

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public AssetsManagerPart (final AssetsManager model) {
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

	/**
	 * A click on the Manager should trigger the call to the corresponding Activity.
	 */
	public void onClick (final View view) {
		NeoComAbstractPart.logger.info(">> [AssetsManagerPart.onClick]");
		final Object managerPart = view.getTag();
		if ( managerPart instanceof AssetsManagerPart ) {
			// Managers have a reference to the Character linked when created.
			//			final NeoComCharacter pilot = ((AssetsManagerPart) managerPart).getCastedModel().getPilot();
			final Intent intent = new Intent(this.getActivity(), AssetsManagerActivity.class);
			// Add all the identification chain as in the backend Angular calls. This is Login/Pilot/Manager
			//			intent.putExtra(AppWideConstants.EExtras.EXTRA_LOGINID.name(), pilot.getLoginRef().getName());
			intent.putExtra(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name(), ((AssetsManagerPart) managerPart).getCastedModel().getCredentialIdentifier());
			getActivity().startActivity(intent);
		}
		NeoComAbstractPart.logger.info("<< [AssetsManagerPart.onClick]");
	}

	public int getActiveIcon () {
		return R.drawable.assets;
	}

	public AssetsManager getCastedModel () {
		return (AssetsManager) this.getModel();
	}

	public int getDimmedIcon () {
		return R.drawable.assetsdimmed;
	}

	@Override
	public long getModelID () {
		return AssetsManagerPart.serialVersionUID;
	}

	public String getName () {
		return "Assets";
	}

	/**
	 * Returns the number of assets that belong to this Character related to the number of records on the local
	 * copy at the database.
	 */
	public String getTAssetsCount () {
		return NeoComAbstractPart.itemCountFormatter.format(this.getCastedModel().getTotalAssetsNumber());
	}

	public String getTRegionCount () {
		return NeoComAbstractPart.itemCountFormatter.format(this.getCastedModel().getRegions().size());
	}

	//	/**
	//	 * A long click should activate the Manager associated activity.
	//	 */
	//	public boolean onLongClick(final View v) {
	//		return false;
	//	}

	@Override
	protected AbstractRender selectRenderer () {
		return new AssetsManagerRender(this, _activity);
	}
}

// - CLASS IMPLEMENTATION ...................................................................................
final class AssetsManagerRender extends NeoComRender {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................

	// - L A Y O U T   F I E L D S
	private ImageView nodeIcon = null;
	private TextView name = null;
	private TextView assetsCount = null;
	private TextView regionCount = null;

	// - L A Y O U T   L A B E L S

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public AssetsManagerRender (final NeoComAbstractPart target, final Activity context) {
		super(target, context);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	@Override
	public AssetsManagerPart getPart () {
		return (AssetsManagerPart) super.getPart();
	}

	@Override
	public void initializeViews () {
		super.initializeViews();
		nodeIcon = (ImageView) _convertView.findViewById(R.id.nodeIcon);
		name = (TextView) _convertView.findViewById(R.id.name);
		assetsCount = (TextView) _convertView.findViewById(R.id.managerAssetsCount);
		regionCount = (TextView) _convertView.findViewById(R.id.managerRegionCount);
	}

	@Override
	public void updateContent () {
		super.updateContent();
		name.setText(this.getPart().getName());
		assetsCount.setText(this.getPart().getTAssetsCount());
		regionCount.setText(this.getPart().getTRegionCount());
		// Needs the check to test of the manager is active to select the right icon.
		if ( this.getPart().checkActivation() ) {
			nodeIcon.setImageDrawable(this.getContext().getResources().getDrawable(this.getPart().getActiveIcon()));
		} else {
			nodeIcon.setImageDrawable(this.getContext().getResources().getDrawable(this.getPart().getDimmedIcon()));
		}
		if ( getPart() instanceof OnClickListener )
			if ( VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP )
				_convertView.setBackground(getContext().getResources().getDrawable(R.drawable.panelbordergreen, getContext().getTheme()));
			else _convertView.setBackground(getContext().getResources().getDrawable(R.drawable.panelbordergreen));
		else if ( VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP )
			_convertView.setBackground(getContext().getResources().getDrawable(R.drawable.panelbordergray, getContext().getTheme()));
		else _convertView.setBackground(getContext().getResources().getDrawable(R.drawable.panelbordergray));
	}

	@Override
	protected void createView () {
		_convertView = inflateView(R.layout.assetsmanager4pilot);
		_convertView.setTag(this);
	}
}

// - UNUSED CODE ............................................................................................
