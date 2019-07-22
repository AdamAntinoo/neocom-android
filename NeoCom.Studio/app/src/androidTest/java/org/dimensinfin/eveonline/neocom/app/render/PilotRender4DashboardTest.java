package org.dimensinfin.eveonline.neocom.app.render;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;

import org.dimensinfin.eveonline.R;
import org.dimensinfin.eveonline.neocom.app.controller.PilotController;
import org.dimensinfin.eveonline.neocom.domain.Pilot;
import org.dimensinfin.eveonline.neocom.support.PilotDashboardSupportTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class PilotRender4DashboardTest extends PilotDashboardSupportTest {
	@Override
	@Before
	public void setUp() throws Exception {
		this.pageNumber = 0;
		Thread.sleep(TimeUnit.SECONDS.toMillis(2));
		super.setUp();
	}

	@Test
	public void pilotAvatar_checkValidity() {
		final View pilotRenderPanel = this.searchPanelByClass(Pilot.class, this.dataSectionContainer);
		Assert.assertNotNull("The panel should be a valid view.", pilotRenderPanel);
		final ImageView pilotAvatar = pilotRenderPanel.findViewById(R.id.avatarIcon);
		Assert.assertNotNull("Check that the filed exists on the view.", pilotAvatar);
		final Drawable obtained = pilotAvatar.getDrawable();

		PilotController pilotRenderController = (PilotController) pilotRenderPanel.getTag(); // Get the controller to reach the Render
		PilotRender4Dashboard pilotRender = (PilotRender4Dashboard) pilotRenderController.buildRender(this.activity);
		// TODO - Cannot get the Picasso running
		Assert.assertNotNull(obtained);
	}

	@Test
	public void cloneType_checkValidity() {
		final View pilotRenderPanel = this.searchPanelByClass(Pilot.class, this.dataSectionContainer);
		Assert.assertNotNull("The panel should be a valid view.", pilotRenderPanel);
		final TextView cloneType = pilotRenderPanel.findViewById(R.id.cloneType);
		Assert.assertNotNull("Check that the filed exists on the view.", cloneType);
		final CharSequence obtained = cloneType.getText();

		final String expected = "Clone Grade Alpha";
		Assert.assertEquals(expected, obtained);
	}

	@Test
	public void pilotName_checkValidity() {
		final View pilotRenderPanel = this.searchPanelByClass(Pilot.class, this.dataSectionContainer);
		Assert.assertNotNull("The panel should be a valid view.", pilotRenderPanel);
		final TextView pilotName = pilotRenderPanel.findViewById(R.id.pilotName);
		Assert.assertNotNull("Check that the filed exists on the view.", pilotName);
		final CharSequence obtained = pilotName.getText();

		PilotController pilotRenderController = (PilotController) pilotRenderPanel.getTag(); // Get the controller to reach the Render
		Pilot pilot = (Pilot) pilotRenderController.getModel();
		final String expected = pilot.getName();
		Assert.assertEquals(expected, obtained);
	}

	@Test
	public void pilotIdentifier_checkValidity() {
		final View pilotRenderPanel = this.searchPanelByClass(Pilot.class, this.dataSectionContainer);
		Assert.assertNotNull("The panel should be a valid view.", pilotRenderPanel);
		final TextView pilotName = pilotRenderPanel.findViewById(R.id.pilotIdentifier);
		Assert.assertNotNull("Check that the filed exists on the view.", pilotName);
		final CharSequence obtained = pilotName.getText();

		PilotController pilotRenderController = (PilotController) pilotRenderPanel.getTag(); // Get the controller to reach the Render
		Pilot pilot = (Pilot) pilotRenderController.getModel();
		final String expected = "[#" + pilot.getPilotIdentifier() + "]";
		Assert.assertEquals(expected, obtained);
	}
}