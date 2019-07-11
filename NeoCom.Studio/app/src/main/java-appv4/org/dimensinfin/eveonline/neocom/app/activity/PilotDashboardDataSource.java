package org.dimensinfin.eveonline.neocom.app.activity;

import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.adapters.ESIDataAdapter;
import org.dimensinfin.eveonline.neocom.app.datasource.AuthenticatedDataSource;
import org.dimensinfin.eveonline.neocom.app.ui.NeoComMenuBar;
import org.dimensinfin.eveonline.neocom.domain.Pilot;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdOk;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetUniverseRaces200Ok;

import java.util.ArrayList;
import java.util.List;

public class PilotDashboardDataSource extends AuthenticatedDataSource {
	// -  C O M P O N E N T S
	private ESIDataAdapter esiDataAdapter;

	@Override
	public void prepareModel() {
		logger.info(">> [PilotDashboardDataSource.prepareModel]");
		final GetCharactersCharacterIdOk publicData = this.esiDataAdapter.getCharactersCharacterId(this.getCredential().getAccountId()
				, this.getCredential().getRefreshToken(), this.getCredential().getDataSource());
		final GetUniverseRaces200Ok raceData = this.esiDataAdapter.searchSDERace(publicData.getRaceId());
		this.addHeaderContents(new Pilot.Builder()
				.withPilotIdentifier(this.getCredential().getAccountId())
				.withCharacterPublicData(publicData)
				.withRaceData(raceData)
				.withCredential(this.getCredential())
				.build());
		this.addHeaderContents(new NeoComMenuBar(this.getCredential()));
		logger.info("<< [PilotDashboardDataSource.prepareModel]");
	}

	/**
	 * Add the list of managers with the core data representing each of them.
	 */
	@Override
	public void collaborate2Model() {
		this.addModelContents(new PlanetaryDashboardManager());
	}

	// - B U I L D E R
	public static class Builder extends AuthenticatedDataSource.Builder<PilotDashboardDataSource, Builder> {
		private PilotDashboardDataSource onConstruction;

		@Override
		protected PilotDashboardDataSource getActual() {
			if (null == this.onConstruction) this.onConstruction = new PilotDashboardDataSource();
			return this.onConstruction;
		}

		@Override
		protected Builder getActualBuilder() {
			return this;
		}

		public Builder withEsiDataAdapter( final ESIDataAdapter esiDataAdapter ) {
			this.onConstruction.esiDataAdapter = esiDataAdapter;
			return this;
		}
	}

	public static class PlanetaryDashboardManager implements ICollaboration {

		@Override
		public List<ICollaboration> collaborate2Model( final String variation ) {
			return new ArrayList<>();
		}

		@Override
		public int compareTo( final Object o ) {
			return 0;
		}
	}
}
