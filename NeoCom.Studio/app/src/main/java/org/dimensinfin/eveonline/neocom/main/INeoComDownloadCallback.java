package org.dimensinfin.eveonline.neocom.main;

import org.dimensinfin.eveonline.neocom.entities.Credential;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdOk;

public interface INeoComDownloadCallback {
	Credential getCredential();

	void sendEvent( PilotDataSections section, Object publicData );
}
