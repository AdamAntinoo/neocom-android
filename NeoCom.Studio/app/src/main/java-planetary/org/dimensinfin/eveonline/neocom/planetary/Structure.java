package org.dimensinfin.eveonline.neocom.planetary;

import java.util.ArrayList;
import java.util.List;

import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.app.adapters.NeoComAndroid;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOkContents;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOkPins;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetUniverseTypesTypeIdOk;
import org.dimensinfin.eveonline.neocom.features.planetaryinteraction.PlanetType;
import org.dimensinfin.eveonline.neocom.features.planetaryinteraction.PlanetarySchematic;
import org.dimensinfin.eveonline.neocom.features.planetaryinteraction.StructureGeoPosition;

import org.joda.time.DateTime;

public class Structure implements ICollaboration {
	private static final GetUniverseTypesTypeIdOk DEFAULT_COMMAND_CENTER = new GetUniverseTypesTypeIdOk();

	static {
		DEFAULT_COMMAND_CENTER.setTypeId(2132);
		DEFAULT_COMMAND_CENTER.setGroupId(1027);
		DEFAULT_COMMAND_CENTER.setName("Barren Command Center");
		DEFAULT_COMMAND_CENTER.setCapacity(0.0F);
	}

	private GetCharactersCharacterIdPlanetsPlanetIdOkPins pinDelegate;
	private double contentsVolume = 0.0;
	private PlanetarySchematic schematic;
	//	private PlanetaryStructureType structureType = PlanetaryStructureType.DEFAULT;
	private GetUniverseTypesTypeIdOk itemDelegate;
	private PlanetType planetType = PlanetType.BARREN;

	private Structure commandCenter;

	// - C O N S T R U C T O R S
	private Structure() {}

	// - G E T T E R S   &   S E T T E R S

	/**
	 * The structure type can be inferred from the item group type instead than form the long list of uniqye types that belong to
	 * a structure type. With this change the number of check elements is quite short.
	 */
	public PlanetaryStructureType getStructureType() {
		return PlanetaryStructureType.getStructureGroup(this.getItemDelegate().getGroupId());
	}

	/** Check that this delegate exists before accessing it. If the type is not accesible then we should use a default one suitable for the structure like the Command Center. */
	public GetUniverseTypesTypeIdOk getItemDelegate() {
		if (null == this.itemDelegate) return DEFAULT_COMMAND_CENTER;
		else return itemDelegate;
	}
	public Structure getCommandCenter() {
		return commandCenter;
	}

	public Structure setCommandCenter( final Structure commandCenter ) {
		this.commandCenter = commandCenter;
		return this;
	}

	public StructureGeoPosition getGeoPosition() {
		return new StructureGeoPosition()
				       .setLongitude(this.getLongitude())
				       .setLatitude(this.getLatitude());
	}

	public double getContentsVolume() {
		return this.contentsVolume;
	}

	// - D E L E G A T I O N
	public List<GetCharactersCharacterIdPlanetsPlanetIdOkContents> getContents() {
		final List<GetCharactersCharacterIdPlanetsPlanetIdOkContents> cont = pinDelegate.getContents();
		if (null == cont) return new ArrayList<>();
		else return cont;
	}

//	public int getContentsUnits() {
//		int units = 0;
//		for (GetCharactersCharacterIdPlanetsPlanetIdOkContents cont : this.getContents())
//			units += cont.getAmount();
//		return units;
//	}

	public int getBackgroundColor() {
		return planetType.getBackgroundColor();
	}

	public int getIconReferenceId() {
		return this.getStructureType().getIconReferenceId();
	}

	public int getIconColorReference() {
		return getStructureType().getIconColorReference();
	}

	public int getStructureColorReference() {
		return this.getStructureType().getStructureRingColorReference();
	}

	public DateTime getLastCycleStart() {
		return pinDelegate.getLastCycleStart();
	}

	public Structure setLastCycleStart( final DateTime lastCycleStart ) {
		pinDelegate.setLastCycleStart(lastCycleStart);
		return this;
	}

	public Float getLatitude() {
		return pinDelegate.getLatitude();
	}

	public Float getLongitude() {
		return pinDelegate.getLongitude();
	}

	public Float getStorageCapacity() {
		return this.getItemDelegate().getCapacity();
	}

	public Integer getSchematicId() {
		return schematic.getSchematicId();
	}

	// - I C O L L A B O R A T I O N   I N T E R F A C E
	@Override
	public List<ICollaboration> collaborate2Model( final String variation ) {
		return new ArrayList<>();
	}

	// - C O R E
	@Override
	public int compareTo( final Object o ) {
		return 0;
	}

	private double calculateContentsVolume() {
		double volumeUsed = 0.0;
		for (GetCharactersCharacterIdPlanetsPlanetIdOkContents cont : this.getContents()) {
			final GetUniverseTypesTypeIdOk contentItem = NeoComAndroid.getSingleton().getEsiAdapter().getUniverseTypeById(cont.getTypeId());
			if (null == contentItem) volumeUsed += cont.getAmount() * 0.1;
			else volumeUsed += cont.getAmount() * contentItem.getVolume();
		}
		return volumeUsed;
	}


	// - B U I L D E R
	public static class Builder {
		private final Structure object;

		public Builder() {
			this.object = new Structure();
		}

		public Builder withPin( final GetCharactersCharacterIdPlanetsPlanetIdOkPins pin ) {
			this.object.pinDelegate = pin;
			// Generate the additional delegated from the current pin data.
			this.withSchematicId(pin.getSchematicId());
//			this.withTypeId(pin.getTypeId());
			// Do costly calculations that may require network access.
			this.object.contentsVolume = this.object.calculateContentsVolume();
			return this;
		}

		/**
		 * This builder method sets the schematic identifier that is the schematic to be used on the production plan for this facility.
		 * Not all structures have the ability to handle schematics to this is an optional method that should code gracefully with the
		 * case the data origin does not define one schematic.
		 *
		 * @param schematicId
		 * @return
		 */
		public Builder withSchematicId( final Integer schematicId ) {
			this.object.schematic = new PlanetarySchematic.Builder(schematicId).build();
			return this;
		}

//		public Builder withTypeId( final Integer typeId ) {
//			this.object.itemDelegate = EveItemProvider.getInstance().search(typeId);
//			//			this.object.structureType = PlanetaryStructureType.getStructureType(typeId);
//			return this;
//		}

		public Builder withPlanetType( final PlanetType planetType ) {
			this.object.planetType = planetType;
			return this;
		}

		public Structure build() {
			return this.object;
		}
	}
}
