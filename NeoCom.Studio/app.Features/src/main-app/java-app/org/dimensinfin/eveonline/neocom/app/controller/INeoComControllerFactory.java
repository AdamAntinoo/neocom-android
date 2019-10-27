package org.dimensinfin.eveonline.neocom.app.controller;

import org.dimensinfin.android.mvc.domain.IControllerFactory;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.updaters.NeoComUpdater;

public interface INeoComControllerFactory extends IControllerFactory {
	NeoComUpdater buildUpdater( ICollaboration model );
}
