//	PROJECT:        NeoCom.Android (NEOC.A)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2017 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API22.
//	DESCRIPTION:		Application to get access to CCP api information and help manage industrial activities
//									for characters and corporations at Eve Online. This is the Android application but shares
//                  libraries and code with other application designed for alternate platforms.
//                  The model management is shown using a generic Model View Controller that allows make the
//                  rendering of the model data similar on all the platforms used.
//                  Latest version adds support for ESI authentication since CREST and other sources will be
//                  removed on 2018.
package org.dimensinfin.eveonline.neocom.model;

import org.dimensinfin.android.model.AbstractViewableNode;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.core.model.AbstractComplexNode;

import java.util.ArrayList;

//- CLASS IMPLEMENTATION ...................................................................................
public class InitializationTask extends AbstractViewableNode implements ICollaboration {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public InitializationTask () {
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	@Override
	public ArrayList<AbstractComplexNode> collaborate2Model (final String variant) {
		return new ArrayList<AbstractComplexNode>();
	}

}
