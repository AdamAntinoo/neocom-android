package org.dimensinfin.eveonline.neocom.planetary;

import java.util.concurrent.TimeUnit;

public enum FacilityStructureInfo {
	BASIC_INDUSTRY(3000, 20, TimeUnit.MINUTES.toMillis(30)),
	ADVANCED_INDUSTRY(40, 5, TimeUnit.MINUTES.toMillis(60)),
	ADVANCED_INDUSTRY_MULTICOMPONENT(10, 3, TimeUnit.MINUTES.toMillis(60)),
	HIGH_INDUSTRY(6, 1, TimeUnit.MINUTES.toMillis(60));

	public static FacilityStructureInfo facility4Schematics( final int schematic ) {
		if (schematic == 104) return ADVANCED_INDUSTRY_MULTICOMPONENT;
		if (schematic == 111) return ADVANCED_INDUSTRY_MULTICOMPONENT;
		if (schematic == 95) return ADVANCED_INDUSTRY_MULTICOMPONENT;
		if (schematic == 110) return ADVANCED_INDUSTRY_MULTICOMPONENT;
		if (schematic == 103) return ADVANCED_INDUSTRY_MULTICOMPONENT;
		if (schematic == 96) return ADVANCED_INDUSTRY_MULTICOMPONENT;

		if (schematic == 117) return HIGH_INDUSTRY;
		if (schematic == 118) return HIGH_INDUSTRY;
		if (schematic == 114) return HIGH_INDUSTRY;
		if (schematic == 112) return HIGH_INDUSTRY;
		if (schematic == 116) return HIGH_INDUSTRY;
		if (schematic == 115) return HIGH_INDUSTRY;
		if (schematic == 113) return HIGH_INDUSTRY;
		if (schematic == 119) return HIGH_INDUSTRY;
		return ADVANCED_INDUSTRY;
	}

	private final long capacity;
	private final long output;
	private final long cycleDuration;

	private FacilityStructureInfo( final long capacity, final long output, final long duration ) {
		this.capacity = capacity;
		this.output = output;
		this.cycleDuration = duration;
	}

	public long getCapacity() {
		return this.capacity;
	}

	public long getOutput() {
		return this.output;
	}

	public long getCycleDuration() {
		return this.cycleDuration;
	}
}
