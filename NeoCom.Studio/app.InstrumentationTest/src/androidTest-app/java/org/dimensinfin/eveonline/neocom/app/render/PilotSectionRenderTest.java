package org.dimensinfin.eveonline.neocom.app.render;

import android.view.View;
import android.widget.TextView;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;

import org.dimensinfin.eveonline.R;
import org.dimensinfin.eveonline.neocom.app.controller.PilotSectionController;
import org.dimensinfin.eveonline.neocom.app.domain.PilotSection;
import org.dimensinfin.eveonline.neocom.support.PilotDashboardSupportTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class PilotSectionRenderTest extends PilotDashboardSupportTest {
	@Override
	@Before
	public void setUp() throws Exception {
		this.pageNumber = 0;
		Thread.sleep(TimeUnit.SECONDS.toMillis(3));
		super.setUp();
	}

	@Test
	public void initializeViews() {
		final View panel = this.searchPanelByClass(PilotSection.class, this.dataSectionContainer);
		Assert.assertNotNull("The panel should be a valid view.", panel);
		PilotSectionController controller = (PilotSectionController) panel.getTag(); // Get the controller to reach the Render
		final PilotSectionRender render = (PilotSectionRender) controller.buildRender(this.activity);
		Assert.assertNotNull(render.sectionIcon);
		Assert.assertNotNull(render.sectionTitle);
		Assert.assertNotNull(render.sectionLabel);
		Assert.assertNotNull(render.sectionData);
	}

	@Test
	public void sectionTitle_checkValidity() {
		final View panel = this.searchPanelByClass(PilotSection.class, this.dataSectionContainer);
		Assert.assertNotNull("The panel should be a valid view.", panel);
		final TextView field = panel.findViewById(R.id.sectionTitle);
		Assert.assertNotNull("Check that the field exists on the view.", field);
		final CharSequence obtained = field.getText();

		PilotSectionController controller = (PilotSectionController) panel.getTag(); // Get the controller to reach the Render
		PilotSection model = (PilotSection) controller.getModel();
		final String expected = model.getTitle();
		Assert.assertEquals(expected, obtained);
	}

	@Test
	public void sectionLabel_checkValidity() {
		final View panel = this.searchPanelByClass(PilotSection.class, this.dataSectionContainer);
		Assert.assertNotNull("The panel should be a valid view.", panel);
		final TextView field = panel.findViewById(R.id.sectionLabel);
		Assert.assertNotNull("Check that the field exists on the view.", field);
		final CharSequence obtained = field.getText();

		PilotSectionController controller = (PilotSectionController) panel.getTag(); // Get the controller to reach the Render
		PilotSection model = (PilotSection) controller.getModel();
		final String expected = model.getLabel();
		Assert.assertEquals(expected, obtained);
	}

	@Test
	public void sectionData_checkValidity() {
		final View panel = this.searchPanelByClass(PilotSection.class, this.dataSectionContainer);
		Assert.assertNotNull("The panel should be a valid view.", panel);
		final TextView field = panel.findViewById(R.id.sectionData);
		Assert.assertNotNull("Check that the field exists on the view.", field);
		final CharSequence obtained = field.getText();

		PilotSectionController controller = (PilotSectionController) panel.getTag(); // Get the controller to reach the Render
		PilotSection model = (PilotSection) controller.getModel();
		final String expected = model.getDataFormatted();
		Assert.assertEquals(expected, obtained);
	}
}
