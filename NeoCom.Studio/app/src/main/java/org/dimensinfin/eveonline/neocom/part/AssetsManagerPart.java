//	PROJECT:        NeoCom.Android (NEOC.A)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2017 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API22.
//	DESCRIPTION:		Application to get access to CCP api information and help manage industrial activities
//									for characters and corporations at Eve Online. This is the Android application but shares
//                  libraries and code with other application designed for alternate platforms.
//                  The model management is shown using a generic Model View Controller that allows make the
//                  rendering of the model data similar on all the platforms used.
package org.dimensinfin.eveonline.neocom.part;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.android.mvc.interfaces.INamedPart;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.activity.AssetsDirectorActivity;
import org.dimensinfin.eveonline.neocom.constant.AppWideConstants;
import org.dimensinfin.eveonline.neocom.interfaces.INeoComDirectorPart;
import org.dimensinfin.eveonline.neocom.manager.AssetsManager;
import org.dimensinfin.eveonline.neocom.model.NeoComCharacter;
import org.dimensinfin.eveonline.neocom.render.NeoComRender;
import org.dimensinfin.eveonline.neocom.storage.NeoComModelStore;

// - CLASS IMPLEMENTATION ...................................................................................
public class AssetsManagerPart extends NeoComAbstractPart
		implements INeoComDirectorPart, INamedPart, OnClickListener /* , OnLongClickListener */ {
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
			final NeoComCharacter pilot = ((AssetsManagerPart) managerPart).getCastedModel().getPilot();
			final Intent intent = new Intent(this.getActivity(), AssetsDirectorActivity.class);
			// Add all the identification chain as in the backend Angular calls. This is Login/Pilot/Manager
			intent.putExtra(AppWideConstants.EExtras.EXTRA_LOGINID.name(), pilot.getLoginRef().getName());
			intent.putExtra(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name(), pilot.getCharacterID());
			//				intent.putExtra(AppWideConstants.EExtras.EXTRA_MANAGERNAME.name(), EManagerType.PLANETARY_MANAGER.name());
			NeoComModelStore.getSingleton().getActivity().startActivity(intent);
		}
		NeoComAbstractPart.logger.info("<< [AssetsManagerPart.onClick]");
	}

	//	/**
	//	 * The Managers do not expand so their collaboration is only themselves.
	//	 */
	//	@Override
	//	public List<IPart> collaborate2View() {
	//		return new ArrayList<IPart>();
	//	}

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
	}

	@Override
	protected void createView () {
		final LayoutInflater mInflater = (LayoutInflater) this.getContext()
		                                                      .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		// The view is a new view. We have to fill all the items
		_convertView = mInflater.inflate(R.layout.assetsmanager4pilot, null);
		_convertView.setTag(this);
	}
}

// - UNUSED CODE ............................................................................................
