package org.dimensinfin.eveonline.neocom.test.support;

import java.util.ArrayList;
import java.util.List;

import org.dimensinfin.android.mvc.controller.IAndroidController;

public class ControllerMatcher<C extends IAndroidController> {
//	private List<IAndroidController> controllerList = new ArrayList<>();

//	public ControllerMatcher( final List<IAndroidController> containerList ) {
////		for (int i = 0; i < containerList.getChildCount(); i++) {
////			final Object item = containerList.getItemAtPosition( i );
//		this.controllerList = containerList;
////		}
//	}

	public List<C> withType( final List<IAndroidController> controllers, final Class<C> type ) {
		final List<C> results = new ArrayList<>();
		for (IAndroidController controller : controllers)
			if (type.isInstance( controller ))
				results.add( (C) controller );
		return results;
	}
//	public List
}
