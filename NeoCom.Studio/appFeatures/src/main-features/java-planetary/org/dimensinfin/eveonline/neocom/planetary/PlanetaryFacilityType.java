package org.dimensinfin.eveonline.neocom.planetary;

import java.util.concurrent.TimeUnit;

import org.dimensinfin.eveonline.neocom.R;

public enum PlanetaryFacilityType {
	DEFAULT(R.drawable.facility80_white
			, R.color.pi_extractoriconcolor, R.color.pi_extractorringcolor
			, TimeUnit.MINUTES.toSeconds(5)),
	COMMAND_CENTER(R.drawable.commandcenter80_white
			, R.color.pi_commandcentericoncolor, R.color.pi_commandcenterringcolor
			, TimeUnit.MINUTES.toSeconds(5)),
	STORAGE(R.drawable.storage80_white
			, R.color.pi_storageiconcolor, R.color.pi_storageringcolor
			, TimeUnit.MINUTES.toSeconds(5)),
	LAUNCHPAD(R.drawable.launchpad80_white
			, R.color.pi_storageiconcolor, R.color.pi_storageringcolor
			, TimeUnit.MINUTES.toSeconds(5)),
	BASIC_INDUSTRY(R.drawable.facility80_white
			, R.color.pi_factoryiconcolor, R.color.pi_factoryringcolor
			, TimeUnit.MINUTES.toSeconds(30)),
	ADVANCED_INDUSTRY(R.drawable.facility80_white
			, R.color.pi_factoryiconcolor, R.color.pi_factoryringcolor
			, TimeUnit.MINUTES.toSeconds(60)),
	EXTRACTOR_HEAD(R.drawable.extractor80_white
			, R.color.pi_extractoriconcolor, R.color.pi_extractorringcolor
			, TimeUnit.MINUTES.toSeconds(5)),
	EXTRACTOR_CONTROL_UNIT(R.drawable.extractor80_white
			, R.color.pi_extractoriconcolor, R.color.pi_extractorringcolor
			, TimeUnit.MINUTES.toSeconds(60));

	public static PlanetaryFacilityType getTypeByStructureGroup( final int structureGroup ) {
		if (structureGroup == 1027) return PlanetaryFacilityType.COMMAND_CENTER;
		if (structureGroup == 2480) return PlanetaryFacilityType.ADVANCED_INDUSTRY;
		if (structureGroup == 2484) return PlanetaryFacilityType.ADVANCED_INDUSTRY;
		if (structureGroup == 2472) return PlanetaryFacilityType.ADVANCED_INDUSTRY;
		if (structureGroup == 1026) return PlanetaryFacilityType.EXTRACTOR_HEAD;
		if (structureGroup == 1028) return PlanetaryFacilityType.BASIC_INDUSTRY;
		if (structureGroup == 1063) return PlanetaryFacilityType.EXTRACTOR_CONTROL_UNIT;
		if (structureGroup == 1030) return PlanetaryFacilityType.LAUNCHPAD;
		if (structureGroup == 1029) return PlanetaryFacilityType.STORAGE;
		return PlanetaryFacilityType.DEFAULT;
	}

	private final int iconReferenceId;
	private final int iconColorReference;
	private final int structureRingColorReference;
	private final long cycleDuration;

	PlanetaryFacilityType( final int iconReferenceId
			, final int iconColorReference
			, final int structureRingColorReference
			, final long duration ) {
		this.iconReferenceId = iconReferenceId;
		this.iconColorReference = iconColorReference;
		this.structureRingColorReference = structureRingColorReference;
		this.cycleDuration = duration;
	}

	public int getIconReferenceId() {
		return this.iconReferenceId;
	}

	public int getIconColorReference() {
		return this.iconColorReference;
	}

	public int getStructureRingColorReference() {
		return this.structureRingColorReference;
	}

//	public long getCycleDuration() {
//		return this.cycleDuration;
//	}
}
