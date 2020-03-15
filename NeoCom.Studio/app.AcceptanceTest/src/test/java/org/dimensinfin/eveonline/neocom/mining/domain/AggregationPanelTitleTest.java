package org.dimensinfin.eveonline.neocom.mining.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AggregationPanelTitleTest {
	@Test
	public void buildComplete() {
		final AggregationPanelTitle aggregationPanelTitle = new AggregationPanelTitle.Builder()
				                                                    .withTitle( "-TITLE-" )
				                                                    .withVolume( 321.65 )
				                                                    .withValue( 987.23 )
				                                                    .build();
		Assertions.assertNotNull( aggregationPanelTitle );
	}

	@Test
	public void buildFailure() {
		Assertions.assertThrows( NullPointerException.class, () -> {
			final AggregationPanelTitle aggregationPanelTitle = new AggregationPanelTitle.Builder()
					                                                    .withTitle( null )
					                                                    .withVolume( 321.65 )
					                                                    .withValue( 987.23 )
					                                                    .build();
		} );
		Assertions.assertThrows( NullPointerException.class, () -> {
			final AggregationPanelTitle aggregationPanelTitle = new AggregationPanelTitle.Builder()
					                                                    .withVolume( 321.65 )
					                                                    .withValue( 987.23 )
					                                                    .build();
		} );
	}

	@Test
	public void gettersContract() {
		final AggregationPanelTitle aggregationPanelTitle = new AggregationPanelTitle.Builder()
				                                                    .withTitle( "-TITLE-" )
				                                                    .withVolume( 321.65 )
				                                                    .withValue( 987.23 )
				                                                    .build();
		Assertions.assertEquals( "-TITLE-", aggregationPanelTitle.getTitle() );
		Assertions.assertEquals( 321.65, aggregationPanelTitle.getTotalVolume() );
		Assertions.assertEquals( 987.23, aggregationPanelTitle.getTotalValue() );
	}
}