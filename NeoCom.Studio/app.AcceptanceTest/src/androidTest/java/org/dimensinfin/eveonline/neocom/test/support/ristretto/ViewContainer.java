package org.dimensinfin.eveonline.neocom.test.support.ristretto;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class ViewContainer {
	private List<View> views=new ArrayList<>(  );

	public ViewMatch onRender( final Class matcher ) {
		final ViewMatch viewMatch = new ViewMatch();
		for (View item : views) {
			final Object render = item.getTag();
			if (null != render)
				if (matcher.isInstance( render ))
					viewMatch.add( item );
		}
		return viewMatch;
	}

	public void add( final View view ) {
		this.views.add( view );
	}
}
