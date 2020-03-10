package org.dimensinfin.eveonline.neocom.test.support.converters;

import java.util.HashMap;
import java.util.Map;

import org.dimensinfin.eveonline.neocom.asset.converter.EsiAssets200Ok2NeoAssetConverter;
import org.dimensinfin.eveonline.neocom.asset.converter.GetCharactersCharacterIdAsset2EsiAssets200OkConverter;
import org.dimensinfin.eveonline.neocom.asset.domain.EsiAssets200Ok;
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
		final GetCharactersCharacterIdAssets200Ok esiAssetOk = new GetCharactersCharacterIdAssets200Ok();
		esiAssetOk.setIsSingleton( Boolean.parseBoolean( cucumberRow.get( IS_SINGLETON ) ) );
		esiAssetOk.setItemId( Long.parseLong( cucumberRow.get( ITEM_ID ) ) );
		esiAssetOk.setLocationFlag( this.decodeLocationFlag( cucumberRow.get( LOCATION_FLAG ) ) );
		esiAssetOk.setLocationType( this.decodeLocationType( cucumberRow.get( LOCATION_TYPE ) ) );
		esiAssetOk.setLocationId( Long.parseLong( cucumberRow.get( LOCATION_ID ) ) );
		esiAssetOk.setQuantity( Integer.parseInt( cucumberRow.get( QUANTITY ) ) );
		esiAssetOk.setTypeId( Integer.parseInt( cucumberRow.get( TYPE_ID ) ) );
		final EsiAssets200Ok newEsiAsset = new GetCharactersCharacterIdAsset2EsiAssets200OkConverter().convert( esiAssetOk );
		return new EsiAssets200Ok2NeoAssetConverter().convert( newEsiAsset );
	}

	private GetCharactersCharacterIdAssets200Ok.LocationFlagEnum decodeLocationFlag( final String flagValue ) {
		return locationFlagMap.get( flagValue );
	}

	private GetCharactersCharacterIdAssets200Ok.LocationTypeEnum decodeLocationType( final String flagValue ) {
		return locationTypeMap.get( flagValue );
	}
}
