package org.dimensinfin.eveonline.neocom.app.domain;

public enum NeoComPreferences {
	DATASOURCE_PREFERENCE("prefkey_dataSourceList");

	private String preferenceKeyName;

	NeoComPreferences( final String keyName ) {
		this.preferenceKeyName = keyName;
	}
}
