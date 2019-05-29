package org.dimensinfin.eveonline.neocom.planetary.datasource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.dimensinfin.android.mvc.datasource.AMVCDataSource;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.datamngmt.ESIGlobalAdapter;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOk;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOkPins;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetUniversePlanetsPlanetIdOk;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetUniverseTypesTypeIdOk;
import org.dimensinfin.eveonline.neocom.features.planetaryinteraction.PlanetType;
import org.dimensinfin.eveonline.neocom.features.planetaryinteraction.StructureGroup;
import org.dimensinfin.eveonline.neocom.planetary.PlanetaryStructureType;
import org.dimensinfin.eveonline.neocom.planetary.Structure;
import org.dimensinfin.eveonline.neocom.sharedcode.EExtras;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

public class PlanetaryColonyDataSource extends AMVCDataSource {
	protected Credential credential;
	protected ESIGlobalAdapter esiAdapter;
	private GetUniversePlanetsPlanetIdOk planet; // The selected colony
	private List<GetCharactersCharacterIdPlanetsPlanetIdOkPins> pins; // List of installations on the selected planet.
	/**
	 * There are a list of planet structures that are from the ESI domain and then this same list of structures but converted
	 * to our model domain. We should keep the ESI domain data because contains the detailed structure information. That detailed infromation
	 * should be connected to the model data as a <b>delegate</b>.
	 */
	private List<Structure> colonyStructures = new ArrayList<>();
	private Structure commandCenter;
	/**
	 * Aggregation map for the selected planet structures. Each entry on the map aggregates the instances and the number of the
	 * map entry type planet planetary interaction structures.
	 */
	private Map<Integer, List<GetCharactersCharacterIdPlanetsPlanetIdOkPins>> structureMap = new HashMap<>();

	// - C O N S T R U C T O R S
	protected PlanetaryColonyDataSource() {}

	// - B U I L D E R   S E T T E R S
	//	protected PlanetaryColonyDataSource setCredential( final Credential credential ) {
	//		this.credential = credential;
	//		return this;
	//	}
	//
	//	public PlanetaryColonyDataSource setEsiAdapter( final ESIGlobalAdapter esiAdapter ) {
	//		this.esiAdapter = esiAdapter;
	//		return this;
	//	}
	//	protected PlanetaryDataSource setPlanetaryRepository( final MiningRepository miningRepository ) {
	//		this.planetaryRepository = miningRepository;
	//		return this;
	//	}

	// - I D A T A S O U R C E   I N T E R F A C E

	/**
	 * To prepare the model for a colony we only should have to read the planet installations for the selected colony. This information should be
	 * present on the extras bundle received and processed by the Activity.
	 */
	@Override
	public void prepareModel() {
		logger.info(">> [PlanetaryColonyDataSource.prepareModel]");
		final int planetIdentifier = this.getExtras().getInt(EExtras.PLANET_IDENTIFIER.name());
		planet = this.esiAdapter.getUniversePlanetsPlanetId(planetIdentifier
				, this.credential.getRefreshToken()
				, this.credential.getDataSource());
		final GetCharactersCharacterIdPlanetsPlanetIdOk structureContainer = this.esiAdapter.getCharactersCharacterIdPlanetsPlanetId(
				credential.getAccountId()
				, planetIdentifier
				, credential.getRefreshToken()
				, credential.getDataSource());
		this.pins = structureContainer.getPins();
		this.colonyStructures = Stream.of(structureContainer.getPins())
				                        .map(pin -> {
					                        // Aggregate pins of identical type into a pin map.
					                        List<GetCharactersCharacterIdPlanetsPlanetIdOkPins> found = this.structureMap.get(pin.getTypeId());
					                        if (null == found) {
						                        found = new ArrayList<>();
						                        found.add(pin);
						                        this.structureMap.put(pin.getTypeId(), found);
					                        } else found.add(pin);
					                        return pin;
				                        })
				                        .map(pin -> {
					                        // Transform each pin into a model structure.
					                        return new Structure.Builder()
							                               .withPin(pin)
							                               .withPlanetType(PlanetType.lookupByType(this.planet.getTypeId()))
							                               .build();
				                        })
				                        .map(structure -> {
					                        // Search for the command center and store it on the field.
					                        if (structure.getStructureType() == PlanetaryStructureType.COMMAND_CENTER)
						                        this.commandCenter = structure;
					                        return structure;
				                        })
				                        .collect(Collectors.toList());
	}

	@Override
	public void collaborate2Model() {
		for (Map.Entry<Integer, List<GetCharactersCharacterIdPlanetsPlanetIdOkPins>> structures : this.structureMap.entrySet()) {
			//			try {
			final GetUniverseTypesTypeIdOk item = this.esiAdapter.getUniverseTypeById(structures.getValue().get(0).getTypeId());
			this.addHeaderContents(new StructureGroup.Builder(item.getName())
					                       .withGroupId(item.getGroupId())
					                       .withIconReference(PlanetaryStructureType.getStructureGroup(item.getGroupId()).getIconReferenceId())
					                       .withGroupCount(structures.getValue().size())
					                       .build());


			//				new PinGroup2StructureGroupConverter().convert(structures.getValue()));
			//			} catch (RuntimeException rtex) {
			//				logger.info("-- [ColonyDataSource.collaborate2Model]>Skip model because there is no item information");
			//			}
		}
		// - S E C T I O N   D A T A
		for (Structure structure : this.colonyStructures) {
			// Transform and insert the structure model into the data source.
			//					final Structure structure = new EsiPin2StructureConverter().convert( structure )
			//							                            .setPlanetType();
			this.addModelContents(structure.setCommandCenter(this.commandCenter));
		}
	}

	// - B U I L D E R
	public static class Builder extends BaseBuilder<PlanetaryColonyDataSource, Builder> {
		private PlanetaryColonyDataSource onConstruction;

		@Override
		protected PlanetaryColonyDataSource getActual() {
			if (null == this.onConstruction)
				this.onConstruction = new PlanetaryColonyDataSource();
			return this.onConstruction;
		}

		@Override
		protected Builder getActualBuilder() {
			return this;
		}

		public Builder withCredential( final Credential credential ) {
			this.onConstruction.credential = credential;
			return this;
		}

		public Builder withEsiAdapter( final ESIGlobalAdapter esiAdapter ) {
			this.onConstruction.esiAdapter = esiAdapter;
			return this;
		}

		@Override
		public PlanetaryColonyDataSource build() {
			// Check additional mandatory fields.
			final PlanetaryColonyDataSource target = super.build();
			Objects.requireNonNull(target.credential);
			Objects.requireNonNull(target.esiAdapter);
			return target;
		}
	}

	//	public final class PinGroup2StructureGroupConverter implements IConverter<List<GetCharactersCharacterIdPlanetsPlanetIdOkPins>, StructureGroup> {
	//		@Override
	//		public StructureGroup convert( final List<GetCharactersCharacterIdPlanetsPlanetIdOkPins> pinList ) {
	//			// For each structure get the Item data definition.
	//			final GetCharactersCharacterIdPlanetsPlanetIdOkPins pin = pinList.get(0);
	//			final GetUniverseTypesTypeIdOk item = this.esi.getUniverseTypeById("tranquility"
	//					, pin.getTypeId());
	//			AppCompatibilityUtils.testAssertNotNull(item);
	//			// Convert each pin group into a header structure aggregator instance.
	//			return new StructureGroup.Builder(item.getName())
	//					       .withGroupId(item.getGroupId())
	//					       .withIconReference(PlanetaryStructureType.getStructureGroup(item.getGroupId()).getIconReferenceId())
	//					       .withGroupCount(pinList.size())
	//					       .build();
	//		}
	//	}
}
