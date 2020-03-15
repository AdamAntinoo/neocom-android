package org.dimensinfin.eveonline.neocom.mining.domain;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.domain.NeoItem;
import org.dimensinfin.eveonline.neocom.domain.space.SpaceSystem;
import org.dimensinfin.eveonline.neocom.industry.Resource;
import org.dimensinfin.eveonline.neocom.miningextraction.domain.MiningExtraction;

public class MiningExtractionAggregatorContainerTest {
	@Test
	public void buildComplete() {
		final MiningExtractionAggregatorContainer miningExtractionAggregatorContainer = new MiningExtractionAggregatorContainer.Builder<Integer>()
				                                                                                .withFacet( 1234 )
				                                                                                .build();
		Assertions.assertNotNull( miningExtractionAggregatorContainer );
	}

	@Test
	public void addPack() {
		// Given
		final NeoItem item = Mockito.mock( NeoItem.class );
		final SpaceSystem spaceSystem = Mockito.mock( SpaceSystem.class );
		final MiningExtraction pack = new MiningExtraction.Builder()
				                              .withNeoItem( item )
				                              .withQuantity( 1000L )
				                              .withExtractionDate( "2020-0-15" )
				                              .withExtractionHour( 20 )
				                              .withOwnerId( 4326543 )
				                              .withSpaceSystem( spaceSystem )
				                              .build();
		// Test
		final MiningExtractionAggregatorContainer<Integer> miningExtractionAggregatorContainer = new MiningExtractionAggregatorContainer.Builder<Integer>()
				                                                                                         .withFacet( 1234 )
				                                                                                         .build();
		final int obtained = miningExtractionAggregatorContainer.addPack( pack );
		// Assertions
		Assertions.assertEquals( 1, obtained );
	}

	@Test
	public void getContentList() {
		// Given
		final NeoItem item = Mockito.mock( NeoItem.class );
		final SpaceSystem spaceSystem = Mockito.mock( SpaceSystem.class );
		final MiningExtraction pack = new MiningExtraction.Builder()
				                              .withNeoItem( item )
				                              .withQuantity( 1000L )
				                              .withExtractionDate( "2020-0-15" )
				                              .withExtractionHour( 20 )
				                              .withOwnerId( 4326543 )
				                              .withSpaceSystem( spaceSystem )
				                              .build();
		// Test
		final MiningExtractionAggregatorContainer<Integer> miningExtractionAggregatorContainer = new MiningExtractionAggregatorContainer.Builder<Integer>()
				                                                                                         .withFacet( 1234 )
				                                                                                         .build();
		miningExtractionAggregatorContainer.addPack( pack );
		final List<MiningExtraction> obtained = miningExtractionAggregatorContainer.getContentList();
		// Assertions
		Assertions.assertEquals( 1, obtained.size() );
		Assertions.assertEquals( 1000, obtained.get( 0 ).getQuantity() );
	}

	@Test
	public void getResourcesValue() {
		// Given
		final NeoItem item = Mockito.mock( NeoItem.class );
		final SpaceSystem spaceSystem = Mockito.mock( SpaceSystem.class );
		final MiningExtraction pack = new MiningExtraction.Builder()
				                              .withNeoItem( item )
				                              .withQuantity( 1000L )
				                              .withExtractionDate( "2020-0-15" )
				                              .withExtractionHour( 20 )
				                              .withOwnerId( 4326543 )
				                              .withSpaceSystem( spaceSystem )
				                              .build();
		// When
		Mockito.when( item.getPrice() ).thenReturn( 123.45 );
		// Test
		final MiningExtractionAggregatorContainer<Integer> miningExtractionAggregatorContainer = new MiningExtractionAggregatorContainer.Builder<Integer>()
				                                                                                         .withFacet( 1234 )
				                                                                                         .build();
		miningExtractionAggregatorContainer.addPack( pack );
		final Double obtained = miningExtractionAggregatorContainer.getResourcesValue();
		// Assertions
		Assertions.assertEquals( 1000L * 123.45, obtained, 0.01 );
	}

	@Test
	public void collaborate2ModelInteger() {
		// Given
		final NeoItem item = Mockito.mock( NeoItem.class );
		final SpaceSystem spaceSystem = Mockito.mock( SpaceSystem.class );
		final MiningExtraction pack = new MiningExtraction.Builder()
				                              .withNeoItem( item )
				                              .withQuantity( 1000L )
				                              .withExtractionDate( "2020-0-15" )
				                              .withExtractionHour( 20 )
				                              .withOwnerId( 4326543 )
				                              .withSpaceSystem( spaceSystem )
				                              .build();
		// When
		Mockito.when( item.getTypeId() ).thenReturn( 4321 );
		// Test
		final MiningExtractionAggregatorContainer<Integer> miningExtractionAggregatorContainer = new MiningExtractionAggregatorContainer.Builder<Integer>()
				                                                                                         .withFacet( 1234 )
				                                                                                         .build();
		miningExtractionAggregatorContainer.addPack( pack );
		final List<ICollaboration> obtained = miningExtractionAggregatorContainer.collaborate2Model( "-VARIANT-" );
		// Assertions
		Assertions.assertTrue( obtained.size() > 0 );
		Assertions.assertTrue( obtained.get( 0 ) instanceof Resource );
	}

	@Test
	public void collaborate2ModelSpaceSystem() {
		// Given
		final NeoItem item = Mockito.mock( NeoItem.class );
		final SpaceSystem spaceSystem = Mockito.mock( SpaceSystem.class );
		// When
		Mockito.when( item.getTypeId() ).thenReturn( 4321 );
		// Test
		final MiningExtractionAggregatorContainer<SpaceSystem> miningExtractionAggregatorContainer = new MiningExtractionAggregatorContainer.Builder<SpaceSystem>()
				                                                                                             .withFacet( spaceSystem )
				                                                                                             .build();
		miningExtractionAggregatorContainer.addPack( new MiningExtraction.Builder()
				                                             .withNeoItem( item )
				                                             .withQuantity( 1000L )
				                                             .withExtractionDate( "2020-0-15" )
				                                             .withExtractionHour( 20 )
				                                             .withOwnerId( 4326543 )
				                                             .withSpaceSystem( spaceSystem )
				                                             .build() );
		miningExtractionAggregatorContainer.addPack( new MiningExtraction.Builder()
				                                             .withNeoItem( item )
				                                             .withQuantity( 2000L )
				                                             .withExtractionDate( "2020-0-15" )
				                                             .withExtractionHour( 21 )
				                                             .withOwnerId( 4326543 )
				                                             .withSpaceSystem( spaceSystem )
				                                             .build() );
		miningExtractionAggregatorContainer.addPack( new MiningExtraction.Builder()
				                                             .withNeoItem( item )
				                                             .withQuantity( 3000L )
				                                             .withExtractionDate( "2020-0-15" )
				                                             .withExtractionHour( 22 )
				                                             .withOwnerId( 4326543 )
				                                             .withSpaceSystem( spaceSystem )
				                                             .build() );
		final List<ICollaboration> obtained = miningExtractionAggregatorContainer.collaborate2Model( "-VARIANT-" );
		// Assertions
		Assertions.assertEquals( 3, miningExtractionAggregatorContainer.getContentList().size() );
		Assertions.assertEquals( 3, obtained.size() );
		Assertions.assertTrue( obtained.get( 0 ) instanceof MiningExtractionAggregatorContainer );
	}

	@Test
	public void getFacetDelegate() {
		// Test
		final MiningExtractionAggregatorContainer<Integer> miningExtractionAggregatorContainer = new MiningExtractionAggregatorContainer.Builder<Integer>()
				                                                                                         .withFacet( 1234 )
				                                                                                         .build();
		final Integer obtained = miningExtractionAggregatorContainer.getFacetDelegate();
		// Assertions
		Assertions.assertEquals( 1234, obtained );
	}

	@Test
	public void buildFailure() {
		Assertions.assertThrows( NullPointerException.class, () -> {
			final MiningExtractionAggregatorContainer miningExtractionAggregatorContainer = new MiningExtractionAggregatorContainer.Builder<Integer>()
					                                                                                .withFacet( null )
					                                                                                .build();
		} );
		Assertions.assertThrows( NullPointerException.class, () -> {
			final MiningExtractionAggregatorContainer miningExtractionAggregatorContainer = new MiningExtractionAggregatorContainer.Builder<Integer>()
					                                                                                .build();
		} );
	}
}