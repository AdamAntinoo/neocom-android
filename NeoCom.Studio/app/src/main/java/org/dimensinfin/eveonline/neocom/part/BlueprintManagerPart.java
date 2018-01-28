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
import android.os.Build;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.android.mvc.interfaces.INamedPart;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.activity.IndustryDirectorActivity;
import org.dimensinfin.eveonline.neocom.constant.AppWideConstants;
import org.dimensinfin.eveonline.neocom.interfaces.INeoComDirectorPart;
import org.dimensinfin.eveonline.neocom.manager.BlueprintManager;
import org.dimensinfin.eveonline.neocom.part.core.NeoComAbstractPart;
import org.dimensinfin.eveonline.neocom.render.NeoComRender;
import org.dimensinfin.eveonline.neocom.storage.NeoComModelStore;

// - CLASS IMPLEMENTATION ...................................................................................
public class BlueprintManagerPart extends NeoComAbstractPart
		implements INeoComDirectorPart, INamedPart, OnClickListener {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static final long serialVersionUID = 7725920841939916584L;

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public BlueprintManagerPart (final BlueprintManager model) {
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

	public int getActiveIcon () {
		return R.drawable.planets;
	}

	public BlueprintManager getCastedModel () {
		return (BlueprintManager) this.getModel();
	}

	public int getDimmedIcon () {
		return R.drawable.industry;
	}

	@Override
	public long getModelID () {
		return BlueprintManagerPart.serialVersionUID;
	}

	public String getName () {
		return "Blueprints T2";
	}

	/**
	 * Returns the number of planetary resources that belong to this Character. This data is obtained from the
	 * <code>PlanetaryManager</code> after checking the database records.
	 */
	public String getTResourcesCount (int valuetoFormat) {
		return NeoComAbstractPart.itemCountFormatter.format(valuetoFormat);
	}

	/**
	 * A click on the Manager should trigger the expand/collapse pattern.
	 */
	public void onClick (final View view) {
		NeoComAbstractPart.logger.info(">> [PlanetaryManagerPart.onClick]");
		final Object managerPart = view.getTag();
		if ( managerPart instanceof BlueprintManagerPart ) {
			// Managers have a reference to the Character linked when created.
			//			final NeoComCharacter pilot = ((BlueprintManagerPart) managerPart).getCastedModel().getPilot();
			final Intent intent = new Intent(this.getActivity(), IndustryDirectorActivity.class);
			// Add all the identification chain as in the backend Angular calls. This is Login/Pilot/Manager
			//			intent.putExtra(AppWideConstants.EExtras.EXTRA_LOGINID.name(), pilot.getLoginRef().getName());
			intent.putExtra(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name(), ((BlueprintManagerPart) managerPart).getCastedModel().getCredentialIdentifier());
			NeoComModelStore.getSingleton().getActivity().startActivity(intent);
		}
		NeoComAbstractPart.logger.info("<< [PlanetaryManagerPart.onClick]");
	}

	protected AbstractRender selectRenderer () {
		return new BlueprintManagerRender(this, _activity);
	}
}

// - CLASS IMPLEMENTATION ...................................................................................
final class BlueprintManagerRender extends NeoComRender {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................

	// - L A Y O U T   F I E L D S
	private ImageView nodeIcon = null;
	private TextView name = null;
	private TextView t1BueprintCount = null;
	private TextView t2BueprintCount = null;
	private TextView t3BueprintCount = null;

	// - L A Y O U T   L A B E L S

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public BlueprintManagerRender (final NeoComAbstractPart target, final Activity context) {
		super(target, context);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	@Override
	public BlueprintManagerPart getPart () {
		return (BlueprintManagerPart) super.getPart();
	}

	@Override
	public void initializeViews () {
		super.initializeViews();
		nodeIcon = (ImageView) _convertView.findViewById(R.id.nodeIcon);
		name = (TextView) _convertView.findViewById(R.id.name);
		t1BueprintCount = (TextView) _convertView.findViewById(R.id.t1BueprintCount);
		t2BueprintCount = (TextView) _convertView.findViewById(R.id.t2BueprintCount);
		t3BueprintCount = (TextView) _convertView.findViewById(R.id.t3BueprintCount);
	}

	@Override
	public void updateContent () {
		super.updateContent();
		name.setText(this.getPart().getName());
		t1BueprintCount.setText(this.getPart().getTResourcesCount(getPart().getCastedModel().getT1Delegate().getBlueprintCount()));
		t2BueprintCount.setText(this.getPart().getTResourcesCount(getPart().getCastedModel().getT2Delegate().getBlueprintCount()));
		//		t3BueprintCount.setText(this.getPart().getTResourcesCount(getPart().getCastedModel().getT3Delegate().getBlueprintCount()));
		// Needs the check to test of the manager is active to select the right icon.
		if ( this.getPart().checkActivation() ) {
			nodeIcon.setImageResource(this.getPart().getActiveIcon());
		} else {
			nodeIcon.setImageResource(this.getPart().getDimmedIcon());
		}
		// Check if the click is active. If not change the border to gray.
		if ( getPart() instanceof OnClickListener )
			if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
				_convertView.setBackground(getContext().getResources().getDrawable(R.drawable.panelbordergreen, getContext().getTheme()));
			} else _convertView.setBackground(getContext().getResources().getDrawable(R.drawable.panelbordergreen));
		else if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
			_convertView.setBackground(getContext().getResources().getDrawable(R.drawable.panelbordergray, getContext().getTheme()));
		} else _convertView.setBackground(getContext().getResources().getDrawable(R.drawable.panelbordergray));
		_convertView.invalidate();
	}

	@Override
	protected void createView () {
		_convertView = inflateView(R.layout.blueprintmanager4pilot);
		_convertView.setTag(this);
	}
}

// - UNUSED CODE ............................................................................................
