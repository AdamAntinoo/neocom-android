package org.dimensinfin.eveonline.neocom.appvisualtest.datasources;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.dimensinfin.android.mvc.datasource.AMVCDataSource;
import org.dimensinfin.eveonline.neocom.adapters.ESIDataAdapter;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.database.repositories.CredentialRepository;
import org.dimensinfin.eveonline.neocom.domain.Pilot;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdOk;

public class VTPilotDataSource extends AMVCDataSource {
	private List<Pilot> testPilots = new ArrayList<>();

	private Credential credential;
	private CredentialRepository credentialRepository;
	private ESIDataAdapter esiDataAdapter;

	@Override
	public void prepareModel() {
		final GetCharactersCharacterIdOk characterPublicData = this.esiDataAdapter.getCharactersCharacterId(this.credential.getAccountId()
				, this.credential.getRefreshToken()
				, this.credential.getDataSource());
		final Pilot pilot = new Pilot.Builder()
									.withPilotIdentifier(this.credential.getAccountId())
				                    .withCharacterPublicData(characterPublicData)
				                    .build();
		this.testPilots.add(pilot);
	}

	@Override
	public void collaborate2Model() {
		for (Pilot pilot : this.testPilots) {
			this.addHeaderContents(pilot);
		}
	}

	// - B U I L D E R
	public static class Builder extends AMVCDataSource.BaseBuilder<VTPilotDataSource, VTPilotDataSource.Builder> {
		private VTPilotDataSource onConstruction;

		@Override
		protected VTPilotDataSource getActual() {
			if (null == this.onConstruction) this.onConstruction = new VTPilotDataSource();
			return this.onConstruction;
		}

		@Override
		protected VTPilotDataSource.Builder getActualBuilder() {
			return this;
		}

		public VTPilotDataSource build() {
			final VTPilotDataSource ds = super.build();
			Objects.requireNonNull(this.onConstruction.credential);
			Objects.requireNonNull(this.onConstruction.esiDataAdapter);
			Objects.requireNonNull(this.onConstruction.credentialRepository);
			return ds;
		}

		public VTPilotDataSource.Builder withCredential( final Credential credential ) {
			Objects.requireNonNull(credential);
			this.onConstruction.credential = credential;
			return this;
		}

		public VTPilotDataSource.Builder withEsiDataAdapter( final ESIDataAdapter esiDataAdapter ) {
			Objects.requireNonNull(esiDataAdapter);
			this.onConstruction.esiDataAdapter = esiDataAdapter;
			return this;
		}

		public VTPilotDataSource.Builder withCredentialRepository( final CredentialRepository credentialRepository ) {
			Objects.requireNonNull(credentialRepository);
			this.onConstruction.credentialRepository = credentialRepository;
			return this;
		}
	}
}
