package org.dimensinfin.eveonline.neocom.planetary.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.android.mvc.interfaces.IRender;
import org.dimensinfin.eveonline.R;
import org.dimensinfin.eveonline.neocom.app.EExtras;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.planetary.controller.ColonyPackController;
import org.dimensinfin.eveonline.neocom.planetary.renders.ColonyPackRender;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class PlanetaryColoniesActivityTest {
	private static final int CAPSULEER_IDENTIFIER = 93813310;
	private Credential credential;

	static {
		System.setProperty("org.mockito.android.target", ApplicationProvider.getApplicationContext().getCacheDir().getPath());
	}

	@Rule
	public ActivityTestRule<PlanetaryColoniesActivity> mActivityRule =
			new ActivityTestRule<PlanetaryColoniesActivity>(PlanetaryColoniesActivity.class) {
				@Override
				protected Intent getActivityIntent() {
					Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
					Intent intent = new Intent(targetContext, PlanetaryColoniesActivity.class);
					intent.putExtra(EExtras.CAPSULEER_IDENTIFIER.name()
							, 93813310); // Credential account identifier
					return intent;
				}
			};

	@Before
	public void setUp() {
		this.credential = new Credential.Builder(93813310)
				.withAccountId(93813310)
				.withAccountName("Perico Tuerto")
				.withAccessToken("P940P9FpVhR8oq2V96D7pbcLzndNWTsAVgVAMt0HE5tJT15zg83MMqfsZhW1yf1XoFn9_IQJN5LrIa3NA90Ifw")
				.withRefreshToken("52HSB2sQiYBOrvaPidnxvnc-DIgT7DP5gUoCEOCW4v61dBfHOrCplfuwma0En0eZsLff2L6OJ6csIDTEQhqDmr0iVB6XmuNloTYhTT2Lx-x15j37Oo91jRrbHiC414DMX2nDPz-JGAdPLDtOzG2-4ofHR61rvw7sGY8Z1CnAgdGexAN6M4ZX93D_UWBEvlFd")
				.withDataSource("tranquility")
				.withScope("publicData esi-location.read_location.v1 esi-location.read_ship_type.v1 esi-mail.read_mail.v1 esi-skills.read_skills.v1 esi-skills.read_skillqueue.v1 esi-wallet.read_character_wallet.v1 esi-wallet.read_corporation_wallet.v1 esi-search.search_structures.v1 esi-clones.read_clones.v1 esi-universe.read_structures.v1 esi-assets.read_assets.v1 esi-planets.manage_planets.v1 esi-fittings.read_fittings.v1 esi-industry.read_character_jobs.v1 esi-markets.read_character_orders.v1 esi-characters.read_blueprints.v1 esi-contracts.read_character_contracts.v1 esi-clones.read_implants.v1 esi-wallet.read_corporation_wallets.v1 esi-characters.read_notifications.v1 esi-corporations.read_divisions.v1 esi-assets.read_corporation_assets.v1 esi-corporations.read_blueprints.v1 esi-contracts.read_corporation_contracts.v1 esi-industry.read_corporation_jobs.v1 esi-markets.read_corporation_orders.v1 esi-industry.read_character_mining.v1 esi-industry.read_corporation_mining.v1")
				.withAssetsCount(1476)
				.withWalletBalance(6.309543632E8)
				.withRaceName("Amarr")
				.build();
	}

	@Test
	public void resourcesMarketValue_initialValue() {
		final PlanetaryColoniesFragment fragment = (PlanetaryColoniesFragment) this.mActivityRule.getActivity().accessPageAdapter().getItem(0); // Get the first page
		final ListView container = fragment.accessDataSectionContainer();
		Assert.assertTrue("The container should have elements.", container.getCount() > 0);
		final View colonyView = this.getViewByPosition(0, container);
		Assert.assertNotNull("Check there is at least a colony view.", colonyView);
		final IAndroidController controller = (IAndroidController) container.getAdapter().getItem(0); // Get the first colony
		Assert.assertNotNull("Check there is at least a colony.", controller);
		final View panelView = controller.getViewCache();
		Assert.assertNotNull("The controller should have a view.", panelView);
		final TextView resourcesMarketValueView = panelView.findViewById(R.id.resourcesMarketValue);
		Assert.assertNotNull("Verify that panel has the right field.", resourcesMarketValueView);

		final String expected = "-";
		final CharSequence obtained = resourcesMarketValueView.getText();
		Assert.assertEquals("Check the value on the field.", expected, obtained);
	}

	@Test
	public void resourcesMarketValue_afterUpdater() throws InterruptedException {
		Thread.sleep(TimeUnit.SECONDS.toMillis(5));
		final PlanetaryColoniesFragment fragment = (PlanetaryColoniesFragment) this.mActivityRule.getActivity().accessPageAdapter().getItem(0); // Get the first page
		final ListView container = fragment.accessDataSectionContainer();
		Assert.assertTrue("The container should have elements.", container.getCount() > 0);
		final View colonyView = this.getViewByPosition(0, container);
		Assert.assertNotNull("Check there is at least a colony view.", colonyView);
		final IAndroidController controller = (IAndroidController) container.getAdapter().getItem(0); // Get the first colony
		Assert.assertNotNull("Check there is at least a colony.", controller);
		final View panelView = controller.getViewCache();
		Assert.assertNotNull("The controller should have a view.", panelView);
		final IRender render = ((ColonyPackController) controller).buildRender(this.mActivityRule.getActivity());
		final TextView resourcesMarketValueView = panelView.findViewById(R.id.resourcesMarketValue);
		Assert.assertNotNull("Verify that panel has the right field.", resourcesMarketValueView);

		final String expected =
				((ColonyPackRender) render).generatePriceString(((ColonyPackController) controller).getModel().getTotalValue());
		final CharSequence obtained = resourcesMarketValueView.getText();
		Assert.assertEquals("Check the value on the field.", expected, obtained);
	}

	private View getViewByPosition( int position, ListView listView ) {
		final int firstListItemPosition = listView.getFirstVisiblePosition();
		final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

		if (position < firstListItemPosition || position > lastListItemPosition) {
			return listView.getAdapter().getView(position, listView.getChildAt(position), listView);
		} else {
			final int childIndex = position - firstListItemPosition;
			return listView.getChildAt(childIndex);
		}
	}
//	@Test
//	public void generateActionBar() {
//		final View actionBar = mActivityRule.getActivity().generateActionBar();
//		Assert.assertNotNull("Action bar view should not be null.", actionBar);
//	}

//	@Test
//	public void actionBarContents() {
//		final Credential credential = Mockito.mock(Credential.class);
//		final PlanetaryColoniesActivity activity = Mockito.mock(PlanetaryColoniesActivity.class);
//		Mockito.when(activity.getCredential()).thenReturn(credential);
//		Mockito.when((credential.getAccountName())).thenReturn("Beth Ripley");
//		Mockito.when(credential.getAccountId()).thenReturn(9222);
////		final View actionBarView = mActivityRule.getActivity().generateActionBar();
////		Assert.assertNotNull("Action bar view should not be null.", actionBarView);
////		final Object controller = actionBarView.getTag();
//		Assert.assertTrue(controller instanceof AssetActionBarController);
//		if (controller instanceof AssetActionBarController) {
//			final PlanetaryActionBar model = ((AssetActionBarController) controller).getModel();
//			Assert.assertEquals("Check the title left.", "Beth Ripley", model.getAppName());
//			Assert.assertNull("Check the title right.", model.getAppName());
//			Assert.assertEquals("Check the subtitle.", "PLANETS LIST", model.getPilotName());
//			Assert.assertEquals("Check the activity icon reference.", "PLANETS LIST", model.getActivityIconReference());
//			Assert.assertEquals("Check the pilot identifier.", 9222, model.getPilotIdentifier());
//		}
//	}
}
