package org.dimensinfin.eveonline.neocom.planetary;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.domain.IItemFacet;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOkExtractorDetails;
import org.dimensinfin.eveonline.neocom.model.EveItem;

public class ExtractorFacility extends PlanetaryFacilityWrapper  {
	private EveItem extractionType;

	public String getExtractingResourceName() {
		final GetCharactersCharacterIdPlanetsPlanetIdOkExtractorDetails details = this.planetaryFacility.getExtractorDetails();
		this.extractionType = new EveItem(details.getProductTypeId());
		return this.extractionType.getName();
	}

	public String getExtractingResourceURLForItem() {
		return this.extractionType.getURLForItem();
	}

	// - I C O L L A B O R A T I O N
	@Override
	public List<ICollaboration> collaborate2Model( final String variant ) {
		return new ArrayList<>();
	}

	// - B U I L D E R
	public static class Builder {
		private ExtractorFacility onConstruction;

		public Builder() {
			this.onConstruction = new ExtractorFacility();
		}

		public Builder withPlanetaryFacility( final IPlanetaryFacility planetaryFacility ) {
			this.onConstruction.planetaryFacility = planetaryFacility;
			final Integer extractTypeId = this.onConstruction.planetaryFacility.getExtractorDetails().getProductTypeId();
			if ( null == extractTypeId)
			this.onConstruction.extractionType = new EveItem();
			return this;
		}

		public ExtractorFacility build() {
			Objects.requireNonNull(this.onConstruction.planetaryFacility);
			return this.onConstruction;
		}

	}
}
