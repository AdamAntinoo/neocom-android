package org.dimensinfin.eveonline.neocom.app.activity;

import org.dimensinfin.android.mvc.datasource.MVCDataSource;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.R;
import org.dimensinfin.eveonline.neocom.adapters.ESIDataAdapter;
import org.dimensinfin.eveonline.neocom.app.domain.IDataProvider;
import org.dimensinfin.eveonline.neocom.app.domain.NeoComDataProvider;
import org.dimensinfin.eveonline.neocom.app.domain.PilotSection;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.domain.Pilot;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdOk;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanets200Ok;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetUniverseRaces200Ok;

import java.util.ArrayList;
import java.util.List;

public class PilotDashboardDataSource extends MVCDataSource {
	// -  C O M P O N E N T S
	private ESIDataAdapter esiDataAdapter;
	private Credential credential;
	private List<PilotSection> sections = new ArrayList<>();

	private Credential getCredential() {
		return credential;
	}

	@Override
	public void prepareModel() {
		logger.info(">> [PilotDashboardDataSource.prepareModel]");
		final GetCharactersCharacterIdOk publicData = this.esiDataAdapter.getCharactersCharacterId(this.getCredential().getAccountId());
		final GetUniverseRaces200Ok raceData = this.esiDataAdapter.searchSDERace(publicData.getRaceId());
		final Pilot pilot = new Pilot.Builder()
				.withPilotIdentifier(this.getCredential().getAccountId())
				.withCharacterPublicData(publicData)
				.withRaceData(raceData)
				.withCredential(this.getCredential())
				.build();
		this.addModelContents(pilot);

		this.sections.add(new PilotSection.Builder()
				.withSectionIcon(R.drawable.wallet)
				.withTitle("WALLET")
				.withLabel("Wallet Balance")
				.withDataProvider(new WalletAmountDataProvider.Builder().withPilot(pilot).build())
				.build());
		this.sections.add(new PilotSection.Builder()
				.withSectionIcon(R.drawable.wallet)
				.withTitle("PLANETARY INTERACTION")
				.withLabel("Available Colonies")
				.withDataProvider(new PlanetaryColonyCountDataProvider.Builder()
						.withPilot(pilot)
						.withEsiDataAdapter(this.esiDataAdapter).build())
				.build());

//		this.addHeaderContents(new NeoComMenuBar(this.getCredential()));
		logger.info("<< [PilotDashboardDataSource.prepareModel]");
	}

	/**
	 * Add the list of managers with the core data representing each of them.
	 */
	@Override
	public void collaborate2Model() {
		for (PilotSection section : this.sections)
			this.addModelContents(section);
	}

	// - B U I L D E R
	public static class Builder extends MVCDataSource.BaseBuilder<PilotDashboardDataSource, Builder> {
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

		public Builder withCredential( final Credential credential ) {
			this.onConstruction.credential = credential;
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

	// - W A L L E T D A T A P R O V I D E R
	public static class WalletAmountDataProvider extends NeoComDataProvider implements IDataProvider {
		private Pilot pilot;

		private WalletAmountDataProvider() {}

		public String getDataFormatted() {
			return this.generatePriceString(this.pilot.getCredential().getWalletBalance());
		}

		// - B U I L D E R
		public static class Builder {
			private WalletAmountDataProvider onConstruction;

			public Builder() {
				this.onConstruction = new WalletAmountDataProvider();
			}

			public Builder withPilot( final Pilot pilot ) {
				this.onConstruction.pilot = pilot;
				return this;
			}

			public WalletAmountDataProvider build() {
				return this.onConstruction;
			}
		}
	}

	public static class PlanetaryColonyCountDataProvider extends NeoComDataProvider implements IDataProvider {
		private Pilot pilot;
		private ESIDataAdapter esiDataAdapter;

		private PlanetaryColonyCountDataProvider() {}

		public Credential getCredential() {return this.pilot.getCredential();}

		public String getDataFormatted() {
			final List<GetCharactersCharacterIdPlanets200Ok> esiColonies = this.esiDataAdapter.getCharactersCharacterIdPlanets(
					this.getCredential().getAccountId(),
					this.getCredential().getRefreshToken(),
					this.getCredential().getDataSource());
			return (qtyFormatter.format(esiColonies.size()) + " colonies").toUpperCase();
		}

		// - B U I L D E R
		public static class Builder {
			private PlanetaryColonyCountDataProvider onConstruction;

			public Builder() {
				this.onConstruction = new PlanetaryColonyCountDataProvider();
			}

			public Builder withPilot( final Pilot pilot ) {
				this.onConstruction.pilot = pilot;
				return this;
			}

			public Builder withEsiDataAdapter( final ESIDataAdapter esiDataAdapter ) {
				this.onConstruction.esiDataAdapter = esiDataAdapter;
				return this;
			}

			public PlanetaryColonyCountDataProvider build() {
				return this.onConstruction;
			}
		}
	}
}
