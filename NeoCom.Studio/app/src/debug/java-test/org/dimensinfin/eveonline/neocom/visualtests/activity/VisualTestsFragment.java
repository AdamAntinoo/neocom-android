package org.dimensinfin.eveonline.neocom.visualtests.activity;

import org.dimensinfin.android.mvc.activity.AMVCPagerFragment;
import org.dimensinfin.android.mvc.datasource.AMVCDataSource;
import org.dimensinfin.android.mvc.datasource.IDataSource;
import org.dimensinfin.android.mvc.interfaces.IControllerFactory;
import org.dimensinfin.eveonline.neocom.app.adapters.NeoComAndroid;
import org.dimensinfin.eveonline.neocom.datamngmt.ESIGlobalAdapter;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOkContents;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOkPins;
import org.dimensinfin.eveonline.neocom.model.EveItem;
import org.dimensinfin.eveonline.neocom.planetary.CommandCenterFacility;
import org.dimensinfin.eveonline.neocom.planetary.PlanetType;
import org.dimensinfin.eveonline.neocom.planetary.PlanetaryFacility;

public class VisualTestsFragment extends AMVCPagerFragment {
	@Override
	public IControllerFactory createFactory() {
		return new VisualTestsControllerFactory(this.getVariant());
	}

	@Override
	public IDataSource createDS() {
		return new VisualTestsGenerator.Builder()
				       .addIdentifier("VISUAL-TESTS")
				       .withFactory(this.getFactory())
				       .build();
	}

	@Override
	public String getSubtitle() {
		return null;
	}

	@Override
	public String getTitle() {
		return null;
	}

	public static class VisualTestsGenerator extends AMVCDataSource {
		private CommandCenterFacility commandCenter;
		private ESIGlobalAdapter esiAdapter;
		private GetCharactersCharacterIdPlanetsPlanetIdOkContents bacteria;

		@Override
		public void prepareModel() {
			final GetCharactersCharacterIdPlanetsPlanetIdOkPins structure = new GetCharactersCharacterIdPlanetsPlanetIdOkPins();
			structure.setPinId(1012696414582L);
			structure.setLatitude(0.0F);
			structure.setLongitude(0.0F);
			structure.setTypeId(2550);
			bacteria = new GetCharactersCharacterIdPlanetsPlanetIdOkContents();
			bacteria.setTypeId(2393);
			bacteria.setAmount(1000L);
			structure.addContentsItem(bacteria);
			this.commandCenter = new CommandCenterFacility.Builder()
					                     .withPlanetaryFacility(new PlanetaryFacility.Builder()
							                                            .withEsiAdapter(this.esiAdapter)
							                                            .withPlanetItem(new EveItem(this.esiAdapter.getUniverseTypeById(2550)))
							                                            .withPlanetType(PlanetType.STORM)
							                                            .withPin(structure)
							                                            .build())
					                     .build();
		}

		@Override
		public void collaborate2Model() {
			this.addModelContents(commandCenter);
		}

		// - B U I L D E R
		public static class Builder extends AMVCDataSource.BaseBuilder<VisualTestsGenerator, VisualTestsGenerator.Builder> {
			private VisualTestsGenerator onConstruction;

			@Override
			protected VisualTestsGenerator getActual() {
				if (null == this.onConstruction)
					this.onConstruction = new VisualTestsGenerator();
				return this.onConstruction;
			}

			@Override
			protected Builder getActualBuilder() {
				return this;
			}

			//			public PlanetaryDataSource.Builder withCredential( final Credential credential ) {
			//				this.onConstruction.credential = credential;
			//				return this;
			//			}
			//
			//			public PlanetaryDataSource.Builder withEsiAdapter( final ESIGlobalAdapter esiAdapter ) {
			//				this.onConstruction.esiAdapter = esiAdapter;
			//				return this;
			//			}

			@Override
			public VisualTestsGenerator build() {
				// Check additional mandatory fields.
				final VisualTestsGenerator target = super.build();
				this.onConstruction.esiAdapter = NeoComAndroid.getSingleton().getEsiAdapter();
				//				Objects.requireNonNull(target.credential);
				//				Objects.requireNonNull(target.esiAdapter);
				return target;
			}
		}
	}
}
