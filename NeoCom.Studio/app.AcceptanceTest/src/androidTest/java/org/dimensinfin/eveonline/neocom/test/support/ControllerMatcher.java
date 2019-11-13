package org.dimensinfin.eveonline.neocom.test.support;

import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import org.dimensinfin.android.mvc.controller.AndroidController;
import org.dimensinfin.android.mvc.controller.IAndroidController;

public class ControllerMatcher<C extends AndroidController> {
	private List<IAndroidController> controllerList = new ArrayList<>();

	public ControllerMatcher( final ListView listContainer ) {
		for (int i = 0; i < listContainer.getChildCount(); i++) {
			final Object item = listContainer.getItemAtPosition( i );
			controllerList.add( (IAndroidController) item );
		}
	}

	public List<C> withType( final Class type ) {
		final List<C> results = new ArrayList<>();
		for (IAndroidController controller : this.controllerList)
			if (type.isInstance( controller ))
				results.add( (C) controller );
		return results;
	}
}
