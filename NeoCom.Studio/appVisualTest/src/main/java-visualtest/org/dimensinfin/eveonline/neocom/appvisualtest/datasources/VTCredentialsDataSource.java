package org.dimensinfin.eveonline.neocom.appvisualtest.datasources;

import java.util.ArrayList;
import java.util.List;

import org.dimensinfin.android.mvc.datasource.AMVCDataSource;
import org.dimensinfin.eveonline.neocom.adapters.ESIDataAdapter;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;

public class VTCredentialsDataSource extends AMVCDataSource {
	private ESIDataAdapter esiDataAdapter;
	private List<Credential> testCredentials = new ArrayList<>();

	@Override
	public void prepareModel() {
		final Credential newCredential = new Credential.Builder(92223647)
				                                 .withCharacterName("New Credential Instance")
				                                 .build();
		final Credential validCredential = new Credential.Builder(92223647)
				                                 .withCharacterName("Complete Instance")
				                                 .build();
		validCredential.setAssetsCount(3241);
		validCredential.setWalletBalance(3876234098.0);
		validCredential.setRaceName("CALDARI");
		this.testCredentials.add(newCredential);
		this.testCredentials.add(validCredential);
	}

	@Override
	public void collaborate2Model() {
		for (Credential cred : testCredentials) {
			this.addModelContents(cred);
		}
	}

	// - B U I L D E R
	public static class Builder extends AMVCDataSource.BaseBuilder<VTCredentialsDataSource, VTCredentialsDataSource.Builder> {
		private VTCredentialsDataSource onConstruction;

		@Override
		protected VTCredentialsDataSource getActual() {
			if (null == this.onConstruction) this.onConstruction = new VTCredentialsDataSource();
			return this.onConstruction;
		}

		@Override
		protected VTCredentialsDataSource.Builder getActualBuilder() {
			return this;
		}

		public VTCredentialsDataSource.Builder withEsiDataAdapter( final ESIDataAdapter esiDataAdapter ) {
			this.onConstruction.esiDataAdapter = esiDataAdapter;
			return this;
		}
	}
}
