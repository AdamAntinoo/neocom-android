package org.dimensinfin.eveonline.neocom.app.controller;

import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.support.TestControllerUpdaterFactory;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class CredentialControllerTest {
	@Test
	public void CredentialController_constructor() {
		final Credential model = new Credential();
		final TestControllerUpdaterFactory factory = new TestControllerUpdaterFactory("TEST-VARIANT");
		final CredentialController controller = new CredentialController(model, factory);
		final CredentialController spy = Mockito.spy(controller);

		Assert.assertNotNull(controller);
//		final NeoComUpdater updater = controller.getUpdater();
//		Assert.assertNotNull(updater);
//		Assert.assertTrue("The updater should be of the right class.", updater instanceof CredentialUpdater);
//		Assert.assertTrue("The updated should be able to send events.", updater instanceof IEventEmitter);

//		final boolean eventSent = updater.sendChangeEvent("TEST-EVENT");
//		Assert.assertTrue(eventSent);
		// TODO - add code to check that the event is sent at job termination.
//		Mockito.verify(spy).propertyChange(Mockito.any(PropertyChangeEvent.class));
	}
}
