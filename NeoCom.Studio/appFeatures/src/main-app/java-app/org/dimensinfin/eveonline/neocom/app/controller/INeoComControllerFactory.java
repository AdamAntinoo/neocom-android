package org.dimensinfin.eveonline.neocom.app.controller;

import org.dimensinfin.android.mvc.interfaces.IControllerFactory;
import org.dimensinfin.eveonline.neocom.adapters.NeoComUpdater;

public interface INeoComControllerFactory extends IControllerFactory {
	NeoComUpdater getUpdater( ICollaboration model );
}
