package org.dimensinfin.eveonline.neocom.test.support.converters;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.dimensinfin.eveonline.neocom.asset.converter.GetCharactersCharacterIdAsset2NeoAssetConverter;
import org.dimensinfin.eveonline.neocom.database.entities.NeoAsset;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdAssets200Ok;

public class CucumberTable2NeoAssetConverter extends CucumberTableConverter<NeoAsset> {
	private static final Map<String, GetCharactersCharacterIdAssets200Ok.LocationFlagEnum> locationFlagMap =
			new HashMap<>();
	private static final Map<String, GetCharactersCharacterIdAssets200Ok.LocationTypeEnum> locationTypeMap =
			new HashMap<>();

	static {
		final GetCharactersCharacterIdAssets200Ok.LocationFlagEnum[] flagValues =
				GetCharactersCharacterIdAssets200Ok.LocationFlagEnum.values();
		for (int i = 0; i < flagValues.length; i++)
			locationFlagMap.put( flagValues[i].toString(), flagValues[i] );
		final GetCharactersCharacterIdAssets200Ok.LocationTypeEnum[] typeValues =
				GetCharactersCharacterIdAssets200Ok.LocationTypeEnum.values();
		for (int i = 0; i < typeValues.length; i++)
			locationTypeMap.put( typeValues[i].toString(), typeValues[i] );
	}

	private static final String IS_SINGLETON = "is_singleton";
	private static final String ITEM_ID = "item_id";
	private static final String LOCATION_FLAG = "location_flag";
	private static final String LOCATION_ID = "location_id";
	private static final String LOCATION_TYPE = "location_type";
	private static final String QUANTITY = "quantity";
	private static final String TYPE_ID = "type_id";

	@Override
	public NeoAsset convert( final Map<String, String> cucumberRow ) {
		final GetCharactersCharacterIdAssets200Ok esiAsset = new GetCharactersCharacterIdAssets200Ok();
		esiAsset.setIsSingleton( Boolean.parseBoolean( cucumberRow.get( IS_SINGLETON ) ) );
		esiAsset.setItemId( Long.parseLong( cucumberRow.get( ITEM_ID ) ) );
		esiAsset.setLocationFlag( this.decodeLocationFlag( cucumberRow.get( LOCATION_FLAG ) ) );
		esiAsset.setLocationType( this.decodeLocationType( cucumberRow.get( LOCATION_TYPE ) ) );
		esiAsset.setLocationId( Long.parseLong( cucumberRow.get( LOCATION_ID ) ) );
		esiAsset.setQuantity( Integer.parseInt( cucumberRow.get( QUANTITY ) ) );
		esiAsset.setTypeId( Integer.parseInt( cucumberRow.get( TYPE_ID ) ) );
		final NeoAsset newAsset;
		try {
			newAsset = new GetCharactersCharacterIdAsset2NeoAssetConverter().convert( esiAsset );
		} catch (final IOException ioe) {
			return null;
		}
		return newAsset;
	}

	private GetCharactersCharacterIdAssets200Ok.LocationFlagEnum decodeLocationFlag( final String flagValue ) {
		return locationFlagMap.get( flagValue );
	}

	private GetCharactersCharacterIdAssets200Ok.LocationTypeEnum decodeLocationType( final String flagValue ) {
		return locationTypeMap.get( flagValue );
	}
}
