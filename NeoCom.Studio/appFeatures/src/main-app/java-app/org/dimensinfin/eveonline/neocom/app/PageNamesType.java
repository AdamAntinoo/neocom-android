package org.dimensinfin.eveonline.neocom.app;

public enum PageNamesType {
	SPLASH("SPLASH DASHBOARD"),
	PILOT_DASHBOARD("PILOT DASHBOARD"),
	MINING_LEDGER("MINING LEDGER"),
	MINING_EXTRACTIONS_TODAY("TODAY MINING EXTRACTIONS"),
	MINING_EXTRACTIONS_ALL("ALL MINING EXTRACTIONS"),
	PLANETS_LIST("PLANET COLONY LIST"),
	PLANET_FACILITIES_DETAILS("PLANET INSTALLATIONS - DETAILS"),
	PLANET_FACILITIES_LAYOUT("PLANET INSTALLATIONS");

	private String pageTitle;

	PageNamesType( final String pageTitle ) {
		this.pageTitle = pageTitle;
	}

	public String getPageTitle() {
		return pageTitle;
	}
}
