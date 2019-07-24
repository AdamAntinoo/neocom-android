package org.dimensinfin.eveonline.neocom.support;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ListView;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.eveonline.neocom.app.EExtras;
import org.dimensinfin.eveonline.neocom.app.activity.PilotDashboardActivity;
import org.dimensinfin.eveonline.neocom.app.activity.PilotDashboardFragment;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;

public class PilotDashboardSupportTest {
	private static final int TEST_PILOT_IDENTIFIER_PERICO_TUERTO = 93813310;
	protected int pageNumber;
	protected PilotDashboardActivity activity;
	protected PilotDashboardFragment fragment;
	protected ListView dataSectionContainer;
	protected Credential credential;

	static {
		System.setProperty("org.mockito.android.target", ApplicationProvider.getApplicationContext().getCacheDir().getPath());
	}

	@Rule
	public ActivityTestRule<PilotDashboardActivity> mActivityRule =
			new ActivityTestRule<PilotDashboardActivity>(PilotDashboardActivity.class) {
				@Override
				protected Intent getActivityIntent() {
					Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
					Intent intent = new Intent(targetContext, PilotDashboardActivity.class);
					intent.putExtra(EExtras.CAPSULEER_IDENTIFIER.name()
							, TEST_PILOT_IDENTIFIER_PERICO_TUERTO); // Credential account identifier
					return intent;
				}
			};
	@Rule
	public GrantPermissionRule mPermissionExternalStorageRule = GrantPermissionRule.grant(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
	@Rule
	public GrantPermissionRule mPermissionInternetRule = GrantPermissionRule.grant(android.Manifest.permission.INTERNET);
	@Rule
	public GrantPermissionRule mPermissionAccessNetworkStateRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_NETWORK_STATE);

	@Before
	public void setUp() throws Exception{
		this.credential = new Credential.Builder(TEST_PILOT_IDENTIFIER_PERICO_TUERTO)
				.withAccountId(TEST_PILOT_IDENTIFIER_PERICO_TUERTO)
				.withAccountName("Perico Tuerto")
				.withAccessToken("P940P9FpVhR8oq2V96D7pbcLzndNWTsAVgVAMt0HE5tJT15zg83MMqfsZhW1yf1XoFn9_IQJN5LrIa3NA90Ifw")
				.withRefreshToken("52HSB2sQiYBOrvaPidnxvnc-DIgT7DP5gUoCEOCW4v61dBfHOrCplfuwma0En0eZsLff2L6OJ6csIDTEQhqDmr0iVB6XmuNloTYhTT2Lx-x15j37Oo91jRrbHiC414DMX2nDPz-JGAdPLDtOzG2-4ofHR61rvw7sGY8Z1CnAgdGexAN6M4ZX93D_UWBEvlFd")
				.withDataSource("tranquility")
				.withScope("publicData esi-location.read_location.v1 esi-location.read_ship_type.v1 esi-mail.read_mail.v1 esi-skills.read_skills.v1 esi-skills.read_skillqueue.v1 esi-wallet.read_character_wallet.v1 esi-wallet.read_corporation_wallet.v1 esi-search.search_structures.v1 esi-clones.read_clones.v1 esi-universe.read_structures.v1 esi-assets.read_assets.v1 esi-planets.manage_planets.v1 esi-fittings.read_fittings.v1 esi-industry.read_character_jobs.v1 esi-markets.read_character_orders.v1 esi-characters.read_blueprints.v1 esi-contracts.read_character_contracts.v1 esi-clones.read_implants.v1 esi-wallet.read_corporation_wallets.v1 esi-characters.read_notifications.v1 esi-corporations.read_divisions.v1 esi-assets.read_corporation_assets.v1 esi-corporations.read_blueprints.v1 esi-contracts.read_corporation_contracts.v1 esi-industry.read_corporation_jobs.v1 esi-markets.read_corporation_orders.v1 esi-industry.read_character_mining.v1 esi-industry.read_corporation_mining.v1")
				.withAssetsCount(1476)
				.withWalletBalance(6.309543632E8)
				.withRaceName("Amarr")
				.build();
		this.activity = this.mActivityRule.getActivity();
		Assert.assertNotNull("Activity exists.", this.activity);
		this.fragment = (PilotDashboardFragment) this.activity.accessPageAdapter().getItem(this.pageNumber); // Get the first page
		Assert.assertNotNull("Fragment exists.", this.fragment);
		this.dataSectionContainer = this.fragment.accessDataSectionContainer();
		Assert.assertNotNull("Container ListView exists.", this.dataSectionContainer);
		Assert.assertTrue("The container should have elements.", this.dataSectionContainer.getCount() > 0);
	}

	protected View searchPanelByClass( final Class filterClass, final ListView container ) {
		final int contentCount = container.getChildCount();
		for (int i = 0; i < contentCount; i++) {
			final View target = this.getViewByPosition(i, container);
			final IAndroidController controller = (IAndroidController) target.getTag();
			final Object model = controller.getModel();
			if (model.getClass() == filterClass) return target;
		}
		return null;
	}

	protected View getViewByPosition( int position, ListView listView ) {
		final int firstListItemPosition = listView.getFirstVisiblePosition();
		final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

		if (position < firstListItemPosition || position > lastListItemPosition) {
			return listView.getAdapter().getView(position, listView.getChildAt(position), listView);
		} else {
			final int childIndex = position - firstListItemPosition;
			return listView.getChildAt(childIndex);
		}
	}
}
