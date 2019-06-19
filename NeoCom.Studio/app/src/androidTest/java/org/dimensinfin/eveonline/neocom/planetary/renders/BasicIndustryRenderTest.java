package org.dimensinfin.eveonline.neocom.planetary.renders;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.dimensinfin.eveonline.neocom.app.EExtras;
import org.dimensinfin.eveonline.neocom.app.adapters.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.database.repositories.CredentialRepository;
import org.dimensinfin.eveonline.neocom.planetary.activity.PlanetaryColoniesActivity;
import org.dimensinfin.eveonline.neocom.planetary.controller.PlanetaryFacilityController;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class BasicIndustryRenderTest {
	static {
		System.setProperty("org.mockito.android.target", ApplicationProvider.getApplicationContext().getCacheDir().getPath());
	}

	@Rule
	public ActivityTestRule<PlanetaryColoniesActivity> mActivityRule =
			new ActivityTestRule<PlanetaryColoniesActivity>(PlanetaryColoniesActivity.class) {
				@Override
				protected Intent getActivityIntent() {
					Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
					//					final Application application = Mockito.mock(Application.class);
					//					NeoComComponentFactory.initialiseSingleton(application);
					final NeoComComponentFactory neoComComponentFactory = Mockito.mock(NeoComComponentFactory.class);
					final CredentialRepository credentialRepository = Mockito.mock(CredentialRepository.class);
					Mockito.when(NeoComComponentFactory.getSingleton()).thenReturn(neoComComponentFactory);
					Mockito.when(neoComComponentFactory.getCredentialRepository()).thenReturn(credentialRepository);
					final Credential credential = Mockito.mock(Credential.class);
					Mockito.when((credential.getAccountName())).thenReturn("Beth Ripley");
					Mockito.when(credential.getAccountId()).thenReturn(9222);
					List<Credential> credentialList = new ArrayList<>();
					credentialList.add(credential);
					Mockito.when(credentialRepository.accessAllCredentials()).thenReturn(credentialList);
					final int identifier = credential.getAccountId();
					final Intent intent = new Intent(targetContext, PlanetaryColoniesActivity.class);
					intent.putExtra(EExtras.CAPSULEER_IDENTIFIER.name(), identifier);
					return intent;
				}
				////				@Override
				//				protected Credential searchCredential( final int identifier ) {
				//					return credential;
				//				}
			};

	@Test
	public void accessLayoutReference() {
		final PlanetaryFacilityController controller = Mockito.mock(PlanetaryFacilityController.class);
		final BasicIndustryRender render = new BasicIndustryRender(controller, mActivityRule.getActivity());
		Assert.assertNotNull("Render is not null.", render);
		final int id = render.accessLayoutReference();
		Assert.assertTrue(id > 0);
	}
}
