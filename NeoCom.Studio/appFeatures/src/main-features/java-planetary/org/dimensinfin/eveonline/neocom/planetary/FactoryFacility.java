package org.dimensinfin.eveonline.neocom.planetary;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.database.repositories.PlanetaryRepository;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOkContents;
import org.dimensinfin.eveonline.neocom.model.EveItem;

public class FactoryFacility extends PlanetaryFacilityWrapper {
	private PlanetaryRepository planetaryRepository;
	private Integer schematicId;
	private PlanetarySchematic schematic;

	private List<FactoryInputs> inputs = new ArrayList<>();

	public List<FactoryInputs> getInputs() {
		return this.inputs;
	}

	// - I C O L L A B O R A T I O N
	@Override
	public List<ICollaboration> collaborate2Model( final String variant ) {
		return new ArrayList<>();
	}

	// - B U I L D E R
	public static class Builder {
		private FactoryFacility onConstruction;

		public Builder() {
			this.onConstruction = new FactoryFacility();
		}

		public Builder withPlanetaryFacility( final PlanetaryFacility planetaryFacility ) {
			this.onConstruction.planetaryFacility = planetaryFacility;
			Objects.requireNonNull(this.onConstruction.planetaryFacility);
			// Convert the contents to the factory list of contents and get Schematics data from repositories.
			for (GetCharactersCharacterIdPlanetsPlanetIdOkContents content : this.onConstruction.planetaryFacility.getContents()) {
				this.onConstruction.inputs.add(new FactoryInputs(content));
			}
			return this;
		}

		public Builder withPlanetaryRepository( final PlanetaryRepository planetaryRepository ) {
			this.onConstruction.planetaryRepository = planetaryRepository;
			return this;
		}

		public Builder withSchematics( final Integer schematicId ) {
			Objects.requireNonNull(schematicId);
			this.onConstruction.schematicId = schematicId;
			return this;
		}

		public FactoryFacility build() {
			Objects.requireNonNull(this.onConstruction.planetaryFacility);
			Objects.requireNonNull(this.onConstruction.schematicId);
			Objects.requireNonNull(this.onConstruction.planetaryRepository);
			// Initialize the schematic
			this.onConstruction.schematic = new PlanetarySchematic.Builder(this.onConstruction.schematicId)
					                                .withPlanetaryRepository(this.onConstruction.planetaryRepository)
					                                .build();
			return this.onConstruction;
		}
	}

	public static class FactoryInputs {
		private GetCharactersCharacterIdPlanetsPlanetIdOkContents planetaryContent;
		private EveItem itemDelegate;

		public FactoryInputs( final GetCharactersCharacterIdPlanetsPlanetIdOkContents content ) {
			this.planetaryContent = content;
			this.itemDelegate = new EveItem(this.planetaryContent.getTypeId());
		}

		public int getTypeId() {
			return itemDelegate.getTypeId();
		}

		public String getName() {
			return itemDelegate.getName();
		}

		public double getPrice() {
			return itemDelegate.getPrice();
		}

		public double getVolume() {
			return itemDelegate.getVolume();
		}

		public String getCategoryName() {
			return itemDelegate.getCategoryName();
		}

		public String getGroupName() {
			return itemDelegate.getGroupName();
		}

		public Long getAmount() {
			return this.planetaryContent.getAmount();
		}
	}
}
