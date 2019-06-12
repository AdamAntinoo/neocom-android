package org.dimensinfin.eveonline.neocom.planetary;

import org.dimensinfin.eveonline.neocom.R;

public enum PlanetType {
	BARREN(R.color.pi_barren_backgroundcolor, 2016, "Barren"),
	TEMPERATE(R.color.pi_barren_backgroundcolor, 2016, "Temperate"),
	OCEANIC(R.color.pi_barren_backgroundcolor, 2016, "Oceanic"),
	ICE(R.color.pi_barren_backgroundcolor, 2016, "Ice"),
	GAS(R.color.pi_barren_backgroundcolor, 2016, "Gas"),
	LAVA(R.color.pi_barren_backgroundcolor, 2016, "Lava"),
	STORM(R.color.pi_barren_backgroundcolor, 2016, "Storm"),
	PLASMA(R.color.pi_barren_backgroundcolor, 2016, "Plasma");

	private final int backgroundColor;
	private final int type;
	private final String prefix;

	PlanetType( final int backgroundColor, final int planetType, final String prefix ) {
		this.backgroundColor = backgroundColor;
		this.type = planetType;
		this.prefix = prefix;
	}

	public int getBackgroundColor() {
		return this.backgroundColor;
	}

	public String getPrefix() {
		return this.prefix;
	}
}
