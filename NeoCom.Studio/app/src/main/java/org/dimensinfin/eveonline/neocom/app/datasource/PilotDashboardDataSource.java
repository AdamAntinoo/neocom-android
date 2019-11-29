package org.dimensinfin.eveonline.neocom.app.datasource;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.dimensinfin.android.mvc.datasource.MVCDataSource;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.annotation.LogEnterExit;
import org.dimensinfin.eveonline.neocom.app.domain.IDataProvider;
import org.dimensinfin.eveonline.neocom.app.domain.PilotSection;
import org.dimensinfin.eveonline.neocom.core.domain.NeoComDataProvider;
import org.dimensinfin.eveonline.neocom.core.domain.PageNamesType;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.domain.Pilot;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdOk;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanets200Ok;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetUniverseRaces200Ok;
import org.dimensinfin.eveonline.neocom.provider.ESIDataProvider;
import org.dimensinfin.eveonline.neocom.provider.ESIUniverseDataProvider;

public class PilotDashboardDataSource extends MVCDataSource {
	private List<PilotSection> sections = new ArrayList<>();
	// -  C O M P O N E N T S
	private ESIDataProvider esiDataAdapter;
	private ESIUniverseDataProvider esiUniverseDataProvider;
	private Credential credential;

	private Credential getCredential() {
		return this.credential;
	}

	public ESIUniverseDataProvider getEsiUniverseDataProvider() {
		return this.esiUniverseDataProvider;
	}

	@LogEnterExit
	@Override
	public void prepareModel() {
		final GetCharactersCharacterIdOk publicData = this.esiDataAdapter.getCharactersCharacterId( this.getCredential().getAccountId() );
		final GetUniverseRaces200Ok raceData = this.esiDataAdapter.searchSDERace( publicData.getRaceId() );
		final Pilot pilot = new Pilot.Builder()
				                    .withPilotIdentifier( this.getCredential().getAccountId() )
				                    .withCharacterPublicData( publicData )
				                    .withRaceData( raceData )
				                    .build();
		this.addModelContents( pilot );

		this.sections.add( new PilotSection.Builder()
				                   .withSectionIcon( R.drawable.wallet )
				                   .withTitle( "WALLET" )
				                   .withLabel( "Wallet Balance" )
				                   .withDataProvider( new WalletAmountDataProvider.Builder().withCredential( this.credential ).build() )
				                   .withPilotIdentifier( this.credential.getAccountId() )
				                   .build() );
		this.sections.add( new PilotSection.Builder()
				                   .withSectionIcon( R.drawable.assets )
				                   .withTitle( "ASSETS" )
				                   .withLabel( "Asset Count" )
				                   .withDataProvider( new AssetCountDataProvider.Builder().withCredential( this.credential ).build() )
				                   .withClickTarget( PageNamesType.ASSETS_BYLOCATION )
				                   .withPilotIdentifier( this.credential.getAccountId() )
				                   .build() );
		this.sections.add( new PilotSection.Builder()
				                   .withSectionIcon( R.drawable.miningledger )
				                   .withTitle( "MINING DATA" )
				                   .withLabel( "Mining Resources market value" )
				                   .withDataProvider( new MiningResourcesValueDataProvider.Builder()
						                                      .withCredential( this.credential ).build() )
//				                   .withClickTarget( PageNamesType.MINING_LEDGER )
				                   .withPilotIdentifier( this.credential.getAccountId() )
				                   .build() );
		this.sections.add( new PilotSection.Builder()
				                   .withSectionIcon( R.drawable.planets )
				                   .withTitle( "PLANETARY INTERACTION" )
				                   .withLabel( "Available Colonies" )
				                   .withDataProvider( new PlanetaryColonyCountDataProvider.Builder()
						                                      .withCredential( this.credential )
						                                      .withEsiDataAdapter( this.esiDataAdapter ).build() )
//				                   .withClickTarget( PageNamesType.PLANET_FACILITIES_DETAILS )
				                   .withPilotIdentifier( this.credential.getAccountId() )
				                   .build() );
		logger.info( "<< [PilotDashboardDataSource.prepareModel]" );
	}

	/**
	 * Add the list of managers with the core data representing each of them.
	 */
	@Override
	public void collaborate2Model() {
		for (PilotSection section : this.sections)
			this.addModelContents( section );
	}

	// - B U I L D E R
	public static class Builder extends MVCDataSource.Builder<PilotDashboardDataSource, PilotDashboardDataSource.Builder> {
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

		public PilotDashboardDataSource.Builder withEsiDataAdapter( final ESIDataProvider esiDataAdapter ) {
			Objects.requireNonNull( esiDataAdapter );
			this.onConstruction.esiDataAdapter = esiDataAdapter;
			return this;
		}

		public PilotDashboardDataSource.Builder withESIUniverseDataProvider( final ESIUniverseDataProvider esiUniverseDataProvider ) {
			Objects.requireNonNull( esiUniverseDataProvider );
			this.onConstruction.esiUniverseDataProvider = esiUniverseDataProvider;
			return this;
		}

		public PilotDashboardDataSource.Builder withCredential( final Credential credential ) {
			Objects.requireNonNull( credential );
			this.onConstruction.credential = credential;
			return this;
		}
	}

	// - W A L L E T D A T A P R O V I D E R
	public static class WalletAmountDataProvider extends NeoComDataProvider implements IDataProvider {
		private Credential credential;

		private WalletAmountDataProvider() {}

		public String getDataFormatted() {
			return this.generatePriceString( this.credential.getWalletBalance() );
		}

		// - B U I L D E R
		public static class Builder {
			private WalletAmountDataProvider onConstruction;

			public Builder() {
				this.onConstruction = new WalletAmountDataProvider();
			}

			public Builder withCredential( final Credential credential ) {
				Objects.requireNonNull( credential );
				this.onConstruction.credential = credential;
				return this;
			}

			public WalletAmountDataProvider build() {
				return this.onConstruction;
			}
		}
	}

	public static class AssetCountDataProvider extends NeoComDataProvider implements IDataProvider {
		private Credential credential;

		private AssetCountDataProvider() {}

		public String getDataFormatted() {
			return qtyFormatter.format( this.credential.getAssetsCount() ) + " ITEMS";
		}

		// - B U I L D E R
		public static class Builder {
			private AssetCountDataProvider onConstruction;

			public Builder() {
				this.onConstruction = new AssetCountDataProvider();
			}

			public AssetCountDataProvider.Builder withCredential( final Credential credential ) {
				Objects.requireNonNull( credential );
				this.onConstruction.credential = credential;
				return this;
			}

			public AssetCountDataProvider build() {
				return this.onConstruction;
			}
		}
	}

	public static class PlanetaryColonyCountDataProvider extends NeoComDataProvider implements IDataProvider {
		private Credential credential;
		private ESIDataProvider esiDataAdapter;

		private PlanetaryColonyCountDataProvider() {}

		public String getDataFormatted() {
			final List<GetCharactersCharacterIdPlanets200Ok> esiColonies = new ArrayList<>();
			this.esiDataAdapter.getCharactersCharacterIdPlanets( this.credential );
			return (qtyFormatter.format( esiColonies.size() ) + " colonies").toUpperCase();
		}

		// - B U I L D E R
		public static class Builder {
			private PlanetaryColonyCountDataProvider onConstruction;

			public Builder() {
				this.onConstruction = new PlanetaryColonyCountDataProvider();
			}

			public PlanetaryColonyCountDataProvider.Builder withCredential( final Credential credential ) {
				Objects.requireNonNull( credential );
				this.onConstruction.credential = credential;
				return this;
			}

			public Builder withEsiDataAdapter( final ESIDataProvider esiDataAdapter ) {
				this.onConstruction.esiDataAdapter = esiDataAdapter;
				return this;
			}

			public PlanetaryColonyCountDataProvider build() {
				return this.onConstruction;
			}
		}
	}

	public static class MiningResourcesValueDataProvider extends NeoComDataProvider implements IDataProvider {
		private Credential credential;

		private MiningResourcesValueDataProvider() {}

		/**
		 * The returned value is the estimated value of all the mineral resources found on the pilot belongings.
		 * This is a quite extensive data processing task since it should retrieve all the assets, filter them and evaluate
		 * its value. Then add all the value to get the final result.
		 *
		 * This is obviously a task for an updater that should store the information on the Credential so it will only be
		 * updated when the values become stale.
		 *
		 * @return the estimated value of all the mineral assets as stored on the Credential or '-'.
		 */
		public String getDataFormatted() {
			return this.generatePriceString( this.credential.getMiningResourcesEstimatedValue() );
		}

		// - B U I L D E R
		public static class Builder {
			private MiningResourcesValueDataProvider onConstruction;

			public Builder() {
				this.onConstruction = new MiningResourcesValueDataProvider();
			}

			public MiningResourcesValueDataProvider.Builder withCredential( final Credential credential ) {
				Objects.requireNonNull( credential );
				this.onConstruction.credential = credential;
				return this;
			}

			public MiningResourcesValueDataProvider build() {
				return this.onConstruction;
			}
		}
	}
}
