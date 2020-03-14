package org.dimensinfin.eveonline.neocom.test.support.converters;

import org.dimensinfin.eveonline.neocom.database.entities.MiningExtractionEntity;

import java.util.Map;

public class CucumberTable2MiningExtractionEntityConverter extends CucumberTableConverter<MiningExtractionEntity> {
	private static final String ID = "id";
	private static final String TYPE_ID = "typeId";
	private static final String SOLAR_SYSTEM_ID = "solarSystemId";
	private static final String QUANTITY = "quantity";
	private static final String EXTRACTION_DATE_NAME = "extractionDateName";
	private static final String EXTRACTION_HOUR = "extractionHour";
	private static final String OWNER_ID = "ownerId";

	@Override
	public MiningExtractionEntity convert( Map<String, String> cucumberRow ) {
		return new MiningExtractionEntity.Builder()
				       .withId( cucumberRow.get( ID ) )
				       .withTypeId( Integer.parseInt( cucumberRow.get( TYPE_ID ) ) )
				       .withSolarSystemId( Integer.parseInt( cucumberRow.get( SOLAR_SYSTEM_ID ) ) )
				       .withQuantity( Long.parseLong( cucumberRow.get( QUANTITY ) ) )
				       .withExtractionDateName( cucumberRow.get( EXTRACTION_DATE_NAME ) )
				       .withExtractionHour( Integer.parseInt( cucumberRow.get( EXTRACTION_HOUR ) ) )
				       .withOwnerId( Integer.parseInt( cucumberRow.get( OWNER_ID ) ) )
				       .build();
	}
}
