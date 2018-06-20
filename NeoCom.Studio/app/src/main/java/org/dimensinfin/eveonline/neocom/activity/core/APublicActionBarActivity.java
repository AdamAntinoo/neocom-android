//  PROJECT:     NeoCom.Android (NEOC.A)
//  AUTHORS:     Adam Antinoo - adamantinoo.git@gmail.com
//  COPYRIGHT:   (c) 2013-2018 by Dimensinfin Industries, all rights reserved.
//  ENVIRONMENT: Android API22.
//  DESCRIPTION: Android Application related to the Eve Online game. The purpose is to download and organize
//               the game data to help capsuleers organize and prioritize activities. The strong points are
//               help at the Industry level tracking and calculating costs and benefits. Also the market
//               information update service will help to identify best prices and locations.
//               Planetary Interaction and Ship fittings are point under development.
//               ESI authorization is a new addition that will give continuity and allow download game data
//               from the new CCP data services.
//               This is the Android application version but shares libraries and code with other application
//               designed for Spring Boot Angular 4 platform.
//               The model management is shown using a generic Model View Controller that allows make the
//               rendering of the model data similar on all the platforms used.
package org.dimensinfin.eveonline.neocom.activity.core;

import android.app.ActionBar;
import android.app.Fragment;
import android.support.annotation.WorkerThread;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.Spanned;
import android.view.View;

import org.dimensinfin.android.mvc.activity.AbstractPagerActivity;
import org.dimensinfin.android.mvc.activity.AbstractPagerFragment;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.database.entity.Credential;
import org.dimensinfin.eveonline.neocom.datamngmt.AndroidGlobalDataManager;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetStatusOk;
import org.dimensinfin.eveonline.neocom.interfaces.IURLIconProvider;
import org.dimensinfin.eveonline.neocom.ui.NeoComActionBar;
import org.dimensinfin.eveonline.neocom.ui.PilotAvatarIconProvider;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class APublicActionBarActivity extends AbstractPagerActivity {
	// - S T A T I C - S E C T I O N ..........................................................................
	@WorkerThread
	public static Spanned serverStatus4Actionbar() {
		GetStatusOk serverStatus = AndroidGlobalDataManager.serverStatus();
		final StringBuffer serverStatusMessage = new StringBuffer()
				.append("<font color='#27AE60'>")
				.append(AndroidGlobalDataManager.SERVER_DATASOURCE.toUpperCase())
				.append("</font>")
				.append(" : ")
				.append(serverStatus.getPlayers()).append(" capsuleers");
		return Html.fromHtml(serverStatusMessage.toString());
	}

	// - F I E L D - S E C T I O N ............................................................................
	protected NeoComActionBar neocomActionBarView = null;

	// - M E T H O D - S E C T I O N ..........................................................................
	protected void setupActionBar( final Credential credential ) {
		logger.info(">> [MiningLedgerActivity.setupActionBar]");
		ActionBar actionbar = getActionBar();
		if ( null != actionbar ) {
			actionbar.show();
			actionbar.setDisplayHomeAsUpEnabled(true);
			// Activate the custom ActionBar
			actionbar.setDisplayShowCustomEnabled(true);
			actionbar.setDisplayShowTitleEnabled(false);

			// Create a new Action Bar view from the layout.
			neocomActionBarView = new NeoComActionBar(R.layout.actionbar_neocombar)
					.setIconTop(new PilotAvatarIconProvider(credential.getAccountId()))
					.setTitleLeft(credential.getAccountName());
			this.getActionBar().setCustomView(neocomActionBarView.getView());
			logger.info("<<>>>> [MiningLedgerActivity.setupActionBar]");
		}
	}

	/**
	 * Replace the update of the title and subtitle by this new that access the custom bar. This modifies the standard behavior of the
	 * AbstractPagerActivity to make it compatible with the Custom Action Bar.
	 */
	@Override
	protected void activateIndicator() {
		// If the Indicator is active then set the listener.
		if ( null != _indicator ) {
			_indicator.setVisibility(View.VISIBLE);
			_indicator.setViewPager(_pageContainer);
			_indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

				public void onPageScrolled( final int arg0, final float arg1, final int arg2 ) {
				}

				public void onPageScrollStateChanged( final int arg0 ) {
				}

				public void onPageSelected( final int position ) {
					if ( null != _actionBar ) {
						neocomActionBarView.setTitleLeft(_pageAdapter.getTitle(position));
						// Clear empty subtitles.
						if ( "" == _pageAdapter.getSubTitle(position) ) {
							neocomActionBarView.setSubtitle("");
						} else {
							neocomActionBarView.setSubtitle(_pageAdapter.getSubTitle(position));
						}
					}
				}
			});
		} else {
			_pageContainer.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

				public void onPageScrolled( final int arg0, final float arg1, final int arg2 ) {
				}

				public void onPageScrollStateChanged( final int arg0 ) {
				}

				public void onPageSelected( final int position ) {
					if ( null != _actionBar ) {
						neocomActionBarView.setTitleLeft(_pageAdapter.getTitle(position));
						// Clear empty subtitles.
						if ( "" == _pageAdapter.getSubTitle(position) ) {
							neocomActionBarView.setSubtitle("");
						} else {
							neocomActionBarView.setSubtitle(_pageAdapter.getSubTitle(position));
						}
					}
				}
			});
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		// Update the menu for the first page.
		updateInitialTitle();
	}
	private void updateInitialTitle() {
		if ( null != neocomActionBarView ) {
			Fragment firstFragment = _pageAdapter.getInitialPage();
			// REFACTOR This IF can be removed once this code works.
			if ( firstFragment instanceof AbstractPagerFragment ) {
				neocomActionBarView.setTitleLeft(((AbstractPagerFragment) firstFragment).getTitle());
				neocomActionBarView.setSubtitle(((AbstractPagerFragment) firstFragment).getSubtitle());
			}
		}
	}

	// --- D E L E G A T E D   M E T H O D S - N E O C O M A C T I O N B A R
	public NeoComActionBar setTitleLeft( String title ) {
		return neocomActionBarView.setTitleLeft(title);
	}

	public NeoComActionBar setTitleRight( String title ) {
		return neocomActionBarView.setTitleRight(title);
	}

	public NeoComActionBar setSubtitle( String subtitle ) {
		return neocomActionBarView.setSubtitle(subtitle);
	}

	public NeoComActionBar setSubtitleSpanned( Spanned subtitleSpanned ) {
		return neocomActionBarView.setSubtitleSpanned(subtitleSpanned);
	}

	public NeoComActionBar setActivityIcon( int iconReference ) {
		return neocomActionBarView.setActivityIcon(iconReference);
	}

	public NeoComActionBar setIconTop( IURLIconProvider provider ) {
		return neocomActionBarView.setIconTop(provider);
	}

	public NeoComActionBar setIconBottom( IURLIconProvider provider ) {
		return neocomActionBarView.setIconBottom(provider);
	}
}
// - UNUSED CODE ............................................................................................
//[01]
