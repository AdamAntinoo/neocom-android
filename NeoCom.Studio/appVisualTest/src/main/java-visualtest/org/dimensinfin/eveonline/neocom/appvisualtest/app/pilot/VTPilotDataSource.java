package org.dimensinfin.eveonline.neocom.appvisualtest.app.pilot;

import org.dimensinfin.android.mvc.datasource.MVCDataSource;
import org.dimensinfin.eveonline.neocom.adapters.ESIDataAdapter;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.database.repositories.CredentialRepository;
import org.dimensinfin.eveonline.neocom.domain.Pilot;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdOk;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VTPilotDataSource extends MVCDataSource {
	private List<Pilot> testPilots = new ArrayList<>();

	private Credential credential;
	private CredentialRepository credentialRepository;
	private ESIDataAdapter esiDataAdapter;

	@Override
	public void prepareModel() {
		final GetCharactersCharacterIdOk characterPublicData = this.esiDataAdapter.getCharactersCharacterId(this.credential.getAccountId());
		final Pilot pilot = new Pilot.Builder()
				.withPilotIdentifier(this.credential.getAccountId())
				.withCharacterPublicData(characterPublicData)
				.withCredential(this.credential)
				.build();
		this.testPilots.add(pilot);
	}

	@Override
	public void collaborate2Model() {
		for (Pilot pilot : this.testPilots) {
			this.addModelContents(pilot);
		}
	}

	// - B U I L D E R
	public static class Builder extends MVCDataSource.BaseBuilder<VTPilotDataSource, VTPilotDataSource.Builder> {
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
