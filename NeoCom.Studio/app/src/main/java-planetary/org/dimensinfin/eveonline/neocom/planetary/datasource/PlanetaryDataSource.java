package org.dimensinfin.eveonline.neocom.planetary.datasource;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.dimensinfin.android.mvc.datasource.AMVCDataSource;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.datamngmt.ESIGlobalAdapter;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanets200Ok;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOk;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetUniversePlanetsPlanetIdOk;
import org.dimensinfin.eveonline.neocom.planetary.ColonyPack;

public class PlanetaryDataSource extends AMVCDataSource {
	protected Credential credential;
	protected ESIGlobalAdapter esiAdapter;
	protected List<ColonyPack> colonies = new ArrayList<>();
	//	protected MiningRepository planetaryRepository; // The planetary persistence repository definition.
	//	private PanelTitle sectionTitle; // The panel title for the data section
	//	protected List<MiningExtraction> extractions;
	//	//	private int extractionsCount = 0; // Number of extractions processed
	//	protected List<MiningExtractionAggregatorAsPanelTitle> extractionsPanels = new ArrayList<>();
	//	private Map<Integer, Resource> resources = new HashMap<>();

	// - C O N S T R U C T O R S
	protected PlanetaryDataSource() {}

	// - B U I L D E R   S E T T E R S
	protected PlanetaryDataSource setCredential( final Credential credential ) {
		this.credential = credential;
		return this;
	}

	public PlanetaryDataSource setEsiAdapter( final ESIGlobalAdapter esiAdapter ) {
		this.esiAdapter = esiAdapter;
		return this;
	}
	//	protected PlanetaryDataSource setPlanetaryRepository( final MiningRepository miningRepository ) {
	//		this.planetaryRepository = miningRepository;
	//		return this;
	//	}

	// - I D A T A S O U R C E   I N T E R F A C E
	@Override
	public void prepareModel() {
		logger.info(">> [PlanetaryDataSource.prepareModel]");
		// Read the colony data and generate the support structureContainer, both for the header and for the main.
		final List<GetCharactersCharacterIdPlanets200Ok> esiColonies = this.esiAdapter.getCharactersCharacterIdPlanets(
				credential.getAccountId()
				, credential.getRefreshToken()
				, credential.getDataSource());
		for (GetCharactersCharacterIdPlanets200Ok colonyEsi : esiColonies) {
			final GetUniversePlanetsPlanetIdOk planetData = this.esiAdapter.getUniversePlanetsPlanetId(this.credential.getAccountId()
					, this.credential.getRefreshToken()
					, this.credential.getDataSource());
			final GetCharactersCharacterIdPlanetsPlanetIdOk structureContainer = this.esiAdapter.getCharactersCharacterIdPlanetsPlanetId(
					credential.getAccountId()
					, colonyEsi.getPlanetId()
					, credential.getRefreshToken()
					, credential.getDataSource());
			if (null != structureContainer) {
				this.colonies.add(new ColonyPack(colonyEsi, planetData, structureContainer));
			}
			//			// Search for only the requested colony.
			//			if (colonyEsi.getPlanetId() == colonyIdentifier) {
			//				this.colony = colonyEsi;
			//				break;
			//			}
		}
		//		// If we have found the colony then get the colony structureContainer.
		//		if (null != this.colony) {
		//			this.pins = structureContainer.getPins();
		//			this.colonyStructures = Stream.of(structureContainer.getPins())
		//					                        .map(pin -> {
		//						                        // Aggregate pins of identical type into a pin map.
		//						                        List<GetCharactersCharacterIdPlanetsPlanetIdOkPins> found = this.structureMap.get(pin.getTypeId());
		//						                        if (null == found) {
		//							                        found = new ArrayList<>();
		//							                        found.add(pin);
		//							                        this.structureMap.put(pin.getTypeId(), found);
		//						                        } else found.add(pin);
		//						                        return pin;
		//					                        })
		//					                        .map(pin -> {
		//						                        // Transform each pin into a model structure.
		//						                        final Structure struc = new Structure.Builder()
		//								                                                .withPin(pin)
		//								                                                .withPlanetType(PlanetType.valueOf(this.colony.getPlanetType().name()))
		//								                                                .build();
		//						                        return struc;
		//					                        })
		//					                        .map(structure -> {
		//						                        // Search for the command center and store it on the field.
		//						                        if (structure.getStructureType() == PlanetaryStructureType.COMMAND_CENTER)
		//							                        this.commandCenter = structure;
		//						                        return structure;
		//					                        })
		//					                        .collect(Collectors.toList());
		//		}
	}

	@Override
	public void collaborate2Model() {
		for (ColonyPack colony : this.colonies)
			this.addModelContents(colony);
	}

	// - C O L O N Y P A C K
	public static class ColonyPack2 {
		private GetCharactersCharacterIdPlanets200Ok colony;
		private GetCharactersCharacterIdPlanetsPlanetIdOk structureContainer;

		public ColonyPack2( final GetCharactersCharacterIdPlanets200Ok colony, final GetCharactersCharacterIdPlanetsPlanetIdOk structureContainer ) {
			this.colony = colony;
			this.structureContainer = structureContainer;
		}
	}

	// - B U I L D E R
	public static class Builder extends AMVCDataSource.BaseBuilder<PlanetaryDataSource, Builder> {
		private PlanetaryDataSource onConstruction;

		@Override
		protected PlanetaryDataSource getActual() {
			if (null == this.onConstruction)
				this.onConstruction = new PlanetaryDataSource();
			return this.onConstruction;
		}

		@Override
		protected Builder getActualBuilder() {
			return this;
		}

		public Builder withCredential( final Credential credential ) {
			this.getActual().setCredential(credential);
			return this;
		}

		public Builder withEsiAdapter( final ESIGlobalAdapter esiAdapter ) {
			this.onConstruction.setEsiAdapter(esiAdapter);
			return this;
		}

		@Override
		public PlanetaryDataSource build() {
			// Check additional mandatory fields.
			final PlanetaryDataSource target = super.build();
			Objects.requireNonNull(target.credential);
			Objects.requireNonNull(target.esiAdapter);
			return target;
		}
	}
}
