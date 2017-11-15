//	PROJECT:        NeoCom.Android (NEOC.A)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2017 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API22.
//	DESCRIPTION:		Application to get access to CCP api information and help manage industrial activities
//									for characters and corporations at Eve Online. This is the Android application but shares
//                  libraries and code with other application designed for alternate platforms.
//                  The model management is shown using a generic Model View Controller that allows make the
//                  rendering of the model data similar on all the platforms used.
package org.dimensinfin.eveonline.neocom.fragment;

import android.graphics.Region;

import org.dimensinfin.activity.AbstractPagerFragment;
import org.dimensinfin.android.mvc.core.PartFactory;
import org.dimensinfin.android.mvc.interfaces.IPart;
import org.dimensinfin.android.mvc.interfaces.IPartFactory;
import org.dimensinfin.core.model.AbstractComplexNode;
import org.dimensinfin.eveonline.neocom.enums.ENeoComVariants;
import org.dimensinfin.eveonline.neocom.model.AppVersion;
import org.dimensinfin.eveonline.neocom.model.InitializationTask;
import org.dimensinfin.eveonline.neocom.part.RegionPart;

//- CLASS IMPLEMENTATION ...................................................................................

/**
 * This Fragment is created with the new MVC structure. As previous fragments will initialize the key data structures required by the core
 * code as the Title, Subtitle, Header contents and List contents providers. But wil many of the new simplifications like the removal of the need
 * to define any DataSource or manage any Part directly.
 */
public class SplashTasksFragment extends AbstractPagerFragment {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................

	//- M E T H O D - S E C T I O N ..........................................................................
	@Override
	public void createFactory() {
		AssetsFragment.logger.info(">> [SplashTasksFragment.createFactory]");
		this.setFactory(new SplashPartFactory(this.getVariant()));
		AssetsFragment.logger.info("<< [SplashTasksFragment.createFactory]");
	}

	@Override
	protected void registerDataSource () {

	}

	@Override
	protected void setHeaderContents () {

	}
}
// - CLASS IMPLEMENTATION ...................................................................................
final class SplashPartFactory extends PartFactory implements IPartFactory {
	// - S T A T I C - S E C T I O N ..........................................................................
//	private static Logger logger = Logger.getLogger("AssetPartFactory");

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public SplashPartFactory(final String variantSelected) {
		super(variantSelected);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	/**
	 * The method should create the matching part for the model received but there is no other place where we
	 * should create the next levels of the hierarchy. So we will create the part transformations here.
	 */
	@Override
	public IPart createPart(final AbstractComplexNode node) {
		logger.info("-- [AssetPartFactory.createPart]> Node class: " + node.getClass().getName());
		if (node instanceof AppVersion ) {
			IPart part = new RegionPart((Region) node).setFactory(this)
			                                          .setRenderMode(ENeoComVariants.valueOf(this.getVariant()).name());
			return part;
		}
		if (node instanceof InitializationTask ) {
			IPart part = new RegionPart((Region) node).setFactory(this)
			                                          .setRenderMode(ENeoComVariants.valueOf(this.getVariant()).name());
			return part;
		}

		// If no part is trapped then call the parent chain until one is found.
		return super.createPart(node);
	}
}

// - UNUSED CODE ............................................................................................
