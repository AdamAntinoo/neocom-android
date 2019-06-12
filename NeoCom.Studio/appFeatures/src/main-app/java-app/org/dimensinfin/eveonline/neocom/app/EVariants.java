package org.dimensinfin.eveonline.neocom.app;

public enum EVariants {
	MINING_LEDGER("PLANET LIST"),
	MINING_EXTRACTIONS_TODAY("PLANET LIST"),
	MINING_EXTRACTIONS_ALL("PLANET LIST"),
	PLANETS_LIST("PLANET LIST"),
	PLANET_FACILITIES("PLANET INSTALLATIONS");

	private String title;

	EVariants( final String title ) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}
}
