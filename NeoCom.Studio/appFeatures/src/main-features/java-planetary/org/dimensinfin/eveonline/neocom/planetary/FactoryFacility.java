package org.dimensinfin.eveonline.neocom.planetary;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOkContents;
import org.dimensinfin.eveonline.neocom.model.EveItem;

public class FactoryFacility extends PlanetaryFacilityWrapper {
//	public enum ExtractorState {
//		EXTRACTING, IDLE
//	}

	private List<FactoryInputs> inputs = new ArrayList<>();

	public List<FactoryInputs> getInputs() {
		return this.inputs;
	}
	//	private GetCharactersCharacterIdPlanetsPlanetIdOkExtractorDetails extractorDetails;
	//	private DateTime installTime;
	//	private ExtractorState state = ExtractorState.IDLE;
	//	private EveItem extractionType;

	//	public ExtractorState getState() {
	//		return this.state;
	//	}
	//
	//	public Integer getCycleTime() {
	//		if (this.state == ExtractorState.EXTRACTING)
	//			return this.extractorDetails.getCycleTime();
	//		else return 0;
	//	}
	//
	//	/**
	//	 * Extractors can be extracting cycling until the whole extraction cycle completes. This long cycle can last for days but the small
	//	 * cycle is about to last 2 or 3 hours.
	//	 * Extractors can be stopped so the cycle information may be invalid when the complete cycle is not specified.
	//	 *
	//	 * @return the date time of the last cycle start instant.
	//	 */
	//	public DateTime getLastCycleStart() {
	//		return this.planetaryFacility.getLastCycleStart();
	//	}
	//
	//	//		@Override
	//	//	public GetCharactersCharacterIdPlanetsPlanetIdOkExtractorDetails getExtractorDetails() {
	//	//		return this.planetaryFacility.pinDelegate.getExtractorDetails();
	//	//	}
	//
	//	public String getExtractingResourceName() {
	//		//		final GetCharactersCharacterIdPlanetsPlanetIdOkExtractorDetails details = this.extractorDetails();
	//		this.extractionType = new EveItem(this.extractorDetails.getProductTypeId());
	//		return this.extractionType.getName();
	//	}
	//
	//	public String getExtractingResourceURLForItem() {
	//		return this.extractionType.getURLForItem();
	//	}

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

		//		public Builder withExtractorDetails( final GetCharactersCharacterIdPlanetsPlanetIdOkPins pin ) {
		//			// Read facility data to check the extractor state.
		//			this.onConstruction.extractorDetails = pin.getExtractorDetails();
		//			this.onConstruction.installTime = pin.getInstallTime();
		//			if (null != this.onConstruction.installTime) {
		//				// Check expired extractors
		//				final DateTime expiryTime = pin.getExpiryTime();
		//				if (null == expiryTime) return this;
		//				if (expiryTime.isBefore(DateTime.now())) return this;
		//				final Integer extractTypeId = this.onConstruction.extractorDetails.getProductTypeId();
		//				if (null == extractTypeId) return this;
		//				this.onConstruction.extractionType = new EveItem(extractTypeId);
		//				this.onConstruction.state = ExtractorState.EXTRACTING;
		//			}
		//			return this;
		//		}

		public FactoryFacility build() {
			Objects.requireNonNull(this.onConstruction.planetaryFacility);
			//			Objects.requireNonNull(this.onConstruction.extractorDetails);
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
