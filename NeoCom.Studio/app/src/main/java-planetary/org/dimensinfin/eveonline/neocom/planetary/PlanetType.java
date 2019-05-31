package org.dimensinfin.eveonline.neocom.planetary;

import org.dimensinfin.eveonline.neocom.R;

public enum PlanetType {
	BARREN(R.color.pi_barren_backgroundcolor, 2016),
	TEMPERATE(R.color.pi_barren_backgroundcolor, 2016),
	OCEANIC(R.color.pi_barren_backgroundcolor, 2016),
	ICE(R.color.pi_barren_backgroundcolor, 2016),
	GAS(R.color.pi_barren_backgroundcolor, 2016),
	LAVA(R.color.pi_barren_backgroundcolor, 2016),
	STORM(R.color.pi_barren_backgroundcolor, 2016),
	PLASMA(R.color.pi_barren_backgroundcolor, 2016);

	private final int backgroundColor;
	private final int type;

	PlanetType( final int backgroundColor, final int planetType ) {
		this.backgroundColor = backgroundColor;
		this.type = planetType;
	}

	public int getBackgroundColor() {
		return this.backgroundColor;
	}
}
