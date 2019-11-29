package org.dimensinfin.eveonline.neocom.test.support;

import org.dimensinfin.android.mvc.controller.IAndroidController;

//@FunctionalInterface
public interface ControllerMatcher {
	boolean matches( IAndroidController item);
}
