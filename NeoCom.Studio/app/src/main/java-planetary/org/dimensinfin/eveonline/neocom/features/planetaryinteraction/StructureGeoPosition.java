package org.dimensinfin.eveonline.neocom.features.planetaryinteraction;

public class StructureGeoPosition {
	private float latitude = 0.0F;
	private float longitude = 0.0F;

	public float getLatitude() {
		return latitude;
	}

	public StructureGeoPosition setLatitude( final float latitude ) {
		this.latitude = latitude;
		return this;
	}

	public StructureGeoPosition setLatitude( final double latitude ) {
		this.latitude = Double.valueOf( latitude ).floatValue();
		return this;
	}

	public float getLongitude() {
		return longitude;
	}

	public StructureGeoPosition setLongitude( final float longitude ) {
		this.longitude = longitude;
		return this;
	}

	public StructureGeoPosition setLongitude( final double longitude ) {
		this.longitude = Double.valueOf( longitude ).floatValue();
		return this;
	}
}
