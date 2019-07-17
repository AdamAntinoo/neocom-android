package org.dimensinfin.eveonline.neocom.planetary.datasource;

import org.dimensinfin.android.mvc.datasource.AMVCDataSource;
import org.dimensinfin.eveonline.neocom.adapters.ESIDataAdapter;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.database.repositories.PlanetaryRepository;

public abstract class PlanetaryCommonDataSource extends AMVCDataSource {
	protected Credential credential;
	protected ESIDataAdapter esiDataAdapter;
	protected PlanetaryRepository planetaryRepository;

	protected ESIDataAdapter getEsiDataAdapter() {
		return this.esiDataAdapter;
	}

	protected Credential getCredential() {
		return credential;
	}
}
