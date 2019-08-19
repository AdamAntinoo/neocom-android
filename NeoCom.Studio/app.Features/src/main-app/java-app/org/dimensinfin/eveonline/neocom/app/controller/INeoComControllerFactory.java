package org.dimensinfin.eveonline.neocom.app.controller;

import org.dimensinfin.android.mvc.interfaces.IControllerFactory;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.core.updaters.NeoComUpdater;

public interface INeoComControllerFactory extends IControllerFactory {
	NeoComUpdater buildUpdater( ICollaboration model );
}
