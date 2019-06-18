package org.dimensinfin.eveonline.neocom.planetary;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOkExtractorDetails;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOkPins;
import org.dimensinfin.eveonline.neocom.model.EveItem;

import org.joda.time.DateTime;

public class ExtractorFacility extends PlanetaryFacilityWrapper {
	public enum ExtractorState {
		EXTRACTING, IDLE
	}

	private GetCharactersCharacterIdPlanetsPlanetIdOkExtractorDetails extractorDetails;
	private DateTime installTime;
	private ExtractorState state = ExtractorState.IDLE;
	private EveItem extractionType;

	public ExtractorState getState() {
		return this.state;
	}

	public Integer getCycleTime() {
		if (this.state == ExtractorState.EXTRACTING)
			return this.extractorDetails.getCycleTime();
		else return 0;
	}

	/**
	 * Extractors can be extracting cycling until the whole extraction cycle completes. This long cycle can last for days but the small
	 * cycle is about to last 2 or 3 hours.
	 * Extractors can be stopped so the cycle information may be invalid when the complete cycle is not specified.
	 *
	 * @return the date time of the last cycle start instant.
	 */
	public DateTime getLastCycleStart() {
		return this.planetaryFacility.getLastCycleStart();
	}

	//		@Override
	//	public GetCharactersCharacterIdPlanetsPlanetIdOkExtractorDetails getExtractorDetails() {
	//		return this.planetaryFacility.pinDelegate.getExtractorDetails();
	//	}

	public String getExtractingResourceName() {
		//		final GetCharactersCharacterIdPlanetsPlanetIdOkExtractorDetails details = this.extractorDetails();
		this.extractionType = new EveItem(this.extractorDetails.getProductTypeId());
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

		public Builder withPlanetaryFacility( final PlanetaryFacility planetaryFacility ) {
			this.onConstruction.planetaryFacility = planetaryFacility;
			return this;
		}

		public Builder withExtractorDetails( final GetCharactersCharacterIdPlanetsPlanetIdOkPins pin ) {
			// Read facility data to check the extractor state.
			this.onConstruction.extractorDetails = pin.getExtractorDetails();
			this.onConstruction.installTime = pin.getInstallTime();
			if (null != this.onConstruction.installTime) {
				// Check expired extractors
				final DateTime expiryTime = pin.getExpiryTime();
				if (null == expiryTime) return this;
				if (expiryTime.isBefore(DateTime.now())) return this;
				final Integer extractTypeId = this.onConstruction.extractorDetails.getProductTypeId();
				if (null == extractTypeId) return this;
				this.onConstruction.extractionType = new EveItem(extractTypeId);
				this.onConstruction.state = ExtractorState.EXTRACTING;
			}
			return this;
		}

		public ExtractorFacility build() {
			Objects.requireNonNull(this.onConstruction.planetaryFacility);
			Objects.requireNonNull(this.onConstruction.extractorDetails);
			return this.onConstruction;
		}
	}
}
