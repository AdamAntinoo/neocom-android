package org.dimensinfin.eveonline.neocom.test.steps;

import android.view.ViewGroup;
import androidx.test.core.app.ApplicationProvider;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.junit.Assert;

import org.dimensinfin.eveonline.neocom.app.controller.AppVersionController;
import org.dimensinfin.eveonline.neocom.test.support.NeoComWorld;
import org.dimensinfin.eveonline.neocom.test.support.Ristretto;
import org.dimensinfin.eveonline.neocom.test.support.validators.AppVersionRenderValidator;

import cucumber.api.java.en.Then;

public class THENTheHeaderSectionHasAPanel {
	static {
		System.setProperty( "org.mockito.android.target", ApplicationProvider.getApplicationContext().getCacheDir().getPath() );
	}

	private NeoComWorld world;

	public THENTheHeaderSectionHasAPanel( final NeoComWorld world ) {
		this.world = world;
		Ristretto.setWorld( this.world );
	}

	@Then("the header section has a {string} panel with the next information")
	public void the_header_section_has_a_panel_with_the_next_information( final String panelModelType,
	                                                                      final List<Map<String, String>> dataTable ) {
		final ViewGroup headerContainer = Objects.requireNonNull( Ristretto.accessHeaderContainer( this.world.getSelectedPage() ) );
		switch (panelModelType) {
			case "AppInfoPanel":
				Assert.assertEquals( 1, Ristretto.onContainer( headerContainer ).onController( AppVersionController.class ).count() );
				Assert.assertTrue( Ristretto.onContainer( headerContainer ).onController( AppVersionController.class ).check(
						view -> new AppVersionRenderValidator.Builder()
								        .withExpectedRowData( dataTable.get( 0 ) )
								        .withTargetView( view )
								        .build()
								        .validate()
				) );
		}
	}
}
