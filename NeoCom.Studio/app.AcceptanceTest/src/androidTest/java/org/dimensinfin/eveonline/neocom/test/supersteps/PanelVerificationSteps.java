package org.dimensinfin.eveonline.neocom.test.supersteps;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.junit.Assert;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.mining.controller.DailyExtractionResourcesContainerController;
import org.dimensinfin.eveonline.neocom.mining.controller.MiningResourceController;
import org.dimensinfin.eveonline.neocom.test.support.NeoComWorld;
import org.dimensinfin.eveonline.neocom.test.support.Ristretto;
import org.dimensinfin.eveonline.neocom.test.support.StepFramework;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;

public class PanelVerificationSteps extends StepFramework {
	private static final String FIELD_IDENTIFIER = "fieldIdentifier";
	private static final String FIELD_VALUE = "fieldValue";

	public PanelVerificationSteps( final NeoComWorld world ) {
		super( world );
	}

	@And("check a action bar {string} with the next contents")
	public void checkAActionBarWithNextContents( final String actionBarType, final List<Map<String, String>> cucumberTable ) {
		switch (actionBarType) {
			case "MiningActionBar":
				Assert.assertTrue( this.validateMiningActionBar( this.world.getActiveActivity().getActionBar().getCustomView(), cucumberTable ) );
				break;
			default:
				throw new NullPointerException( "The panel type is not defined on the list of registered verifiers." );
		}
	}

	private boolean validateMiningActionBar( final View actionBar, final List<Map<String, String>> cucumberTable ) {
		Assert.assertNotNull( actionBar );
		for (Map<String, String> row : cucumberTable) {
			TextView field;
			switch (row.get( FIELD_IDENTIFIER )) {
				case "titleTopLeft":
					field = actionBar.findViewById( R.id.toolBarTitleLeft );
					Assert.assertEquals( row.get( FIELD_VALUE ), field.getText() );
					return true;
				case "subtitle":
					field = actionBar.findViewById( R.id.toolBarSubTitle );
					Assert.assertEquals( row.get( FIELD_VALUE ), field.getText() );
					return true;
			}
		}
		return false;
	}

	private boolean validateMiningResourcePanel( final View panel, final List<Map<String, String>> cucumberTable ) {
		final String RESOURCENAME = "resourceName";
		final String RESOURCEIDENTIFIER = "resourceIdentifier";
		final String QUANTITY = "quantity";
		final String VALUE = "value";
		Assert.assertNotNull( panel );
		final Map<String, String> row = cucumberTable.get( 0 );
		Assert.assertEquals( row.get( RESOURCENAME ),
				Objects.requireNonNull( (TextView) panel.findViewById( R.id.resourceName ) ).getText().toString()  );
		Assert.assertEquals( row.get( RESOURCEIDENTIFIER ),
				Objects.requireNonNull( (TextView) panel.findViewById( R.id.resourceIdentifier ) ).getText().toString()  );
		Assert.assertEquals( row.get( QUANTITY ),
				Objects.requireNonNull( (TextView) panel.findViewById( R.id.quantity ) ).getText().toString()  );
		Assert.assertEquals( row.get( VALUE ),
				Objects.requireNonNull( (TextView) panel.findViewById( R.id.value ) ).getText().toString()  );
		return true;
	}

	@And("check a {string} panel with the next contents")
	public void checkAPanelWithTheNextContents( final String panelTypeName, final List<Map<String, String>> cucumberTable ) {
		List<IAndroidController> panelsMatching;
		switch (panelTypeName) {
			case "MiningResource":
				panelsMatching = Ristretto.withType( Ristretto.accessHeaderPanels( this.world.getSelectedPage() ),
						MiningResourceController.class );
				Assert.assertNotNull( panelsMatching );
				Assert.assertTrue( panelsMatching.size() > 0 );
				Assert.assertTrue( this.validateMiningResourcePanel( panelsMatching.get( 0 ).getViewCache(), cucumberTable ) );
				break;
			case "DailyExtractionResourcesContainer":
				panelsMatching = Ristretto.withType( Ristretto.accessHeaderPanels( this.world.getSelectedPage() ),
						DailyExtractionResourcesContainerController.class );
				Assert.assertNotNull( panelsMatching );
				Assert.assertTrue( panelsMatching.size() > 0 );
				Assert.assertTrue( this.validateDailyExtractionResourcesContainerPanel( panelsMatching.get( 0 ).getViewCache(), cucumberTable ) );
				break;
			default:
				throw new NullPointerException( "The panel type is not defined on the list of registered verifiers." );
		}
	}

	private boolean validateDailyExtractionResourcesContainerPanel( final View panel,
	                                                                final List<Map<String, String>> cucumberTable ) {
		final String NAMETYPE_LABEL = "nameTypeLabel";
		final String QUANTITY_LABEL = "quantityLabel";
		final String VALUE_LABEL = "valueLabel";
		Assert.assertNotNull( panel );
		final Map<String, String> row = cucumberTable.get( 0 );
		Assert.assertTrue( row.get( NAMETYPE_LABEL ).equals(
				Objects.requireNonNull( (TextView) panel.findViewById( R.id.nameTypeLabel ) ).getText().toString().toUpperCase() ) );
		Assert.assertTrue( row.get( QUANTITY_LABEL ).equals(
				Objects.requireNonNull( (TextView) panel.findViewById( R.id.quantityLabel ) ).getText().toString().toUpperCase() ) );
		Assert.assertTrue( row.get( VALUE_LABEL ).equals(
				Objects.requireNonNull( (TextView) panel.findViewById( R.id.valueLabel ) ).getText().toString().toUpperCase() ) );
		return true;
	}

	@And("render a panel with {string} and the next contents")
	public void renderAPanelWithAndTheNextContents( final String renderClassName,
	                                                final List<Map<String, String>> cucumberTable ) {
		View panel;
		switch (renderClassName) {
			case "ResourceAggregatorContainer4EsiLocationFacetRender":
			case "ResourceAggregatorContainer4LocalTimeFacetRender":
				panel = this.searchFirstPanelByRenderClass( renderClassName, this.world.getDataSectionContainer() );
				Assert.assertNotNull( panel );
//				Assert.assertTrue(new ResourceAggregatorContainer4EsiLocationFacetPanelValidator.Builder()
//						                  .withPanel(panel)
//						                  .withCucumberTable(cucumberTable)
//						                  .build()
//						                  .validate());
				break;
			default:
				throw new NullPointerException( "The panel type is not defined on the list of registered verifiers." );
		}
	}

	@Then("the header container has {string} panels")
	public void theHeaderContainerHasPanels( final String panelCount ) {
		final ViewGroup header = this.world.getHeaderContainer();
		Assert.assertEquals( Integer.parseInt( panelCount ), header.getChildCount() );
	}

	@Then("the data container has {string} panels")
	public void theDataContainerHasPanels( final String panelCount ) {
		final ListView data = this.world.getDataSectionContainer();
		Assert.assertEquals( Integer.parseInt( panelCount ), data.getChildCount() );
	}
}
