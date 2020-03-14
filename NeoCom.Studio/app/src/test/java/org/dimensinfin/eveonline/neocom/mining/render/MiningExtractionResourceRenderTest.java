package org.dimensinfin.eveonline.neocom.mining.render;

import android.content.Context;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.runner.AndroidJUnitRunner;

import javax.annotation.security.RunAs;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.mining.controller.MiningResourceController;
//@RunWith(AndroidJUnitRunner.class)
public class MiningExtractionResourceRenderTest {
	@Test
	public void constructorContract() {
		final MiningResourceController controller = Mockito.mock( MiningResourceController.class );
		final Context context = Mockito.mock( Context.class );
		final MiningExtractionResourceRender miningExtractionResourceRender = new MiningExtractionResourceRender(
				controller, context );
		Assertions.assertNotNull( miningExtractionResourceRender );
	}

	@Test
	public void getController() {
		// Given
		final MiningResourceController controller = Mockito.mock( MiningResourceController.class );
		final Context context = Mockito.mock( Context.class );
		// Test
		final MiningExtractionResourceRender miningExtractionResourceRender = new MiningExtractionResourceRender(
				controller, context );
		final IAndroidController obtained = miningExtractionResourceRender.getController();
		// Assertions
		Assertions.assertNotNull( obtained );
	}

	@Test
	public void initializeViews() {
		// Given
		final MiningResourceController controller = Mockito.mock( MiningResourceController.class );
//		final Context context = Mockito.mock( Context.class );
		final Context context = ApplicationProvider.getApplicationContext();

		// Test
		final MiningExtractionResourceRender miningExtractionResourceRender = new MiningExtractionResourceRender(
				controller, context );
		miningExtractionResourceRender.initializeViews();
		// Assertions

	}

	@Test
	public void accessLayoutReference() {
		// Given
		final MiningResourceController controller = Mockito.mock( MiningResourceController.class );
		final Context context = Mockito.mock( Context.class );
		// Test
		final MiningExtractionResourceRender miningExtractionResourceRender = new MiningExtractionResourceRender(
				controller, context );
		final int obtained = miningExtractionResourceRender.accessLayoutReference();
		// Assertions
		Assertions.assertEquals( R.layout.resource4miningextractionlist, obtained );
	}
}
