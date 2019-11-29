package org.dimensinfin.eveonline.neocom.test.support;

import org.dimensinfin.android.mvc.controller.IAndroidController;

//@FunctionalInterface
public interface IControllerMatcher {
	boolean matches( IAndroidController item);
}
