package org.dimensinfin.eveonline.neocom.test.support.ristretto;

import android.widget.LinearLayout;

public class NewRistretto {
	public static ViewContainer onContainer( final LinearLayout linear ) {
		final ViewContainer container = new ViewContainer();
		for (int i = 0; i < linear.getChildCount(); i++)
			container.add( linear.getChildAt( i ) );
		return container;
	}
}
