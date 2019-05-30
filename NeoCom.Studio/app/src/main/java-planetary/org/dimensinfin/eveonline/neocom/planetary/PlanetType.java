package org.dimensinfin.eveonline.neocom.planetary;

import java.util.HashMap;
import java.util.Map;

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

	private static final Map<Integer, PlanetType> planetIndex = new HashMap<>();

	private final int backgroundColor;
	private final int type;

	PlanetType( final int backgroundColor, final int planetType ) {
		this.backgroundColor = backgroundColor;
		this.type = planetType;
		setPlanetType(planetType, this);
	}

	public int getBackgroundColor() {
		return this.backgroundColor;
	}

	public int getType() {
		return type;
	}

	public static PlanetType lookupByType( final int type ) {
		return planetIndex.get(type);
	}

	private static void setPlanetType( final int planetType, final PlanetType type ) {
		planetIndex.put(planetType, type);
	}
}
