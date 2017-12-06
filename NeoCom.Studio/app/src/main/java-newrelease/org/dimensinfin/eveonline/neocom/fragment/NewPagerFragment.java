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

import org.dimensinfin.android.mvc.activity.AbstractPagerFragment;
import org.dimensinfin.android.mvc.datasource.MVCDataSource;
import org.dimensinfin.core.datasource.ModelGeneratorStore;
import org.dimensinfin.core.interfaces.IModelGenerator;

//- CLASS IMPLEMENTATION ...................................................................................

/**
 * This Fragment is created with the new MVC structure. As previous fragments will initialize the key data structures
 * required by the core code as the Title, Subtitle, Header contents and List contents providers. But wil many of the
 * new simplifications like the removal of the need to define any DataSource or manage any Part directly.
 */
public abstract class NewPagerFragment extends AbstractPagerFragment {
	// - F I E L D - S E C T I O N ............................................................................
	protected IModelGenerator _generator = null;

	// - C O N S T R U C T O R - S E C T I O N ................................................................

	// - M E T H O D - S E C T I O N ..........................................................................
	public void setGenerator (IModelGenerator fragmentModelGenerator) {
		// Check if the generator already exists on the Cache. If so get the current one and discard the new.
		_generator = ModelGeneratorStore.registerGenerator(fragmentModelGenerator);

		// Generate the DataSource from the Generator and set it up on the Frgment.
		MVCDataSource ds = new MVCDataSource(fragmentModelGenerator.getDataSourceLocator(), getFactory(), fragmentModelGenerator);
		ds.setVariant(this.getVariant());
		setDataSource(ds);
	}
}
