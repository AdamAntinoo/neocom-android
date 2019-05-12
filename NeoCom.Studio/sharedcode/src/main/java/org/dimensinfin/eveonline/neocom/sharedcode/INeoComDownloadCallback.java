package org.dimensinfin.eveonline.neocom.sharedcode;

import org.dimensinfin.eveonline.neocom.entities.Credential;
import org.dimensinfin.eveonline.neocom.sharedcode.PilotDataSections;

public interface INeoComDownloadCallback {
	Credential getCredential();

	void sendEvent( PilotDataSections section, Object publicData );
}
