package org.dimensinfin.eveonline.neocom.test.steps;

import android.view.ViewGroup;
import androidx.test.core.app.ApplicationProvider;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.junit.Assert;

import org.dimensinfin.eveonline.neocom.app.controller.PilotController;
import org.dimensinfin.eveonline.neocom.test.support.NeoComWorld;
import org.dimensinfin.eveonline.neocom.test.support.PanelTypes;
import org.dimensinfin.eveonline.neocom.test.support.Ristretto;
import org.dimensinfin.eveonline.neocom.test.support.validators.Pilot4DashboardRenderValidator;

import cucumber.api.java.en.Then;

public class THENTheDataSectionHasAPanel {
	static {
		System.setProperty( "org.mockito.android.target", ApplicationProvider.getApplicationContext().getCacheDir().getPath() );
	}

	private NeoComWorld world;

	public THENTheDataSectionHasAPanel( final NeoComWorld world ) {
		this.world = world;
		Ristretto.setWorld( this.world );
	}

	@Then("the data section has a {string} panel with the next information")
	public void the_data_section_has_a_panel_with_the_next_information( final String panelModelType,
	                                                                    final List<Map<String, String>> dataTable ) {
		final ViewGroup dataContainer = Objects.requireNonNull( Ristretto.accessDataContainer( this.world.getSelectedPage() ) );
		Assert.assertNotNull( dataContainer );

//		final List<PilotController> targetPanelList = new ControllerMatcher<PilotController>()
//				                                              .withType( Ristretto.accessDataPanels( this.world.getSelectedPage() ),
//						                                              PilotController.class );
//		Assert.assertFalse( dataContainer.isEmpty() );
		switch (PanelTypes.decodeName( panelModelType )) {
			case PILOTCONTROLLER:
				Assert.assertEquals( 1, Ristretto.onContainer( dataContainer ).onController( PilotController.class ).count() );
				Assert.assertTrue( Ristretto.onContainer( dataContainer ).onController( PilotController.class ).check(
						view -> new Pilot4DashboardRenderValidator.Builder()
								        .withExpectedRowData( dataTable.get( 0 ) )
								        .withTargetView( view )
								        .build()
								        .validate()
				) );
//
//
//				final PilotController pilotController = (PilotController) targetPanelList.get( 0 );
//				Assert.assertTrue( this.validatePilot4DashboardRender( dataTable.get( 0 ),
//						Objects.requireNonNull( pilotController.getViewCache() ) ) );
		}
	}

//	private boolean validatePilot4DashboardRender( final Map<String, String> expectedData,
//	                                               final View obtainedView ) {
//		final String PILOT_NAME = "PilotName";
//		final String PILOT_IDENTIFIER = "PilotId";
//		final String PILOT_RACE = "PilotRace";
//		final String CORPORATION_NAME = "CorporationName";
//		Assert.assertEquals( expectedData.get( PILOT_NAME ),
//				((TextView) obtainedView.findViewById( R.id.pilotName )).getText() );
//		Assert.assertEquals( expectedData.get( PILOT_IDENTIFIER ),
//				((TextView) obtainedView.findViewById( R.id.pilotIdentifier )).getText() );
//		Assert.assertEquals( expectedData.get( PILOT_RACE ),
//				((TextView) obtainedView.findViewById( R.id.raceName )).getText() );
//		Assert.assertEquals( expectedData.get( CORPORATION_NAME ),
//				((TextView) obtainedView.findViewById( R.id.corporationName )).getText() );
//
//		return true;
//	}
}
