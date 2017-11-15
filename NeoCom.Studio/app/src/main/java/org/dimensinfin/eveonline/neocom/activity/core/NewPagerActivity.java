//	PROJECT:        NeoCom.Android (NEOC.A)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2017 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API22.
//	DESCRIPTION:		Application to get access to CCP api information and help manage industrial activities
//									for characters and corporations at Eve Online. This is the Android application but shares
//                  libraries and code with other application designed for alternate platforms.
//                  The model management is shown using a generic Model View Controller that allows make the
//                  rendering of the model data similar on all the platforms used.
package org.dimensinfin.eveonline.neocom.activity.core;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.viewpagerindicator.CirclePageIndicator;

import org.dimensinfin.activity.AbstractFragmentPagerAdapter;
import org.dimensinfin.activity.AbstractPagerActivity;
import org.dimensinfin.eveonline.neocom.R;

import java.util.logging.Logger;

/**
 * This class will be used temporarily to adapt the current NeoCom Activities to a new and definite MVC schema. The code will use the new Pager Activity layout (probably the new Tabbed and not the ViewPagerIndicator originally used and now becoming obsolete even it can still be used and it is supported.
 */
//- CLASS IMPLEMENTATION ...................................................................................
public abstract class NewPagerActivity extends AbstractPagerActivity {
	// - S T A T I C - S E C T I O N ..........................................................................
	public static Logger logger = Logger.getLogger("NeoComSplashActivity");

	// - M E T H O D - S E C T I O N ..........................................................................
	protected void onCreate (@Nullable final Bundle savedInstanceState) {
		logger.info(">> [NewPagerActivity.onCreate]"); //$NON-NLS-1$
		super.onCreate(savedInstanceState);

		// Change the layout to be used on this new Activity. Instead the default layout use the latest and new one.
		this.setContentView(R.layout.activity_basepager);
		try {
			// Hide the Action Bar because this page does not need to show it.
	//		_actionBar.hide();
			_actionBar.setVisibility(View.GONE);

			// Locate the elements of the page and store in global data.
			_pageContainer = (ViewPager) this.findViewById(R.id.fragmentContainer);
			_back = (ImageView) this.findViewById(R.id.pagerBackground);
			_indicator = (CirclePageIndicator) this.findViewById(R.id.pagerIndicator);
			// Check page structure.
			if (null == _pageContainer) {
				this.stopActivity(new RuntimeException("RTEX> Expected UI element not found."));
			}
			if (null == _back) {
				this.stopActivity(new RuntimeException("RTEX> Expected UI element not found."));
			}
			// Add the adapter for the page switching.
			_pageAdapter = new AbstractFragmentPagerAdapter(this.getSupportFragmentManager(), _pageContainer.getId());
			_pageContainer.setAdapter(_pageAdapter);
			this.disableIndicator();
		} catch (final Exception rtex) {
			logger.info("RTEX [NewPagerActivity.onCreate]> " + rtex.getMessage());
			rtex.printStackTrace();
			this.stopActivity(new RuntimeException("RTEX [AbstractPagerActivity.onCreate]> " + rtex.getMessage()));
		}

		// Reinitialize the tile and subtitle from the first page.
		this.updateInitialTitle();
		logger.info("<< [NewPagerActivity.onCreate]"); //$NON-NLS-1$
	}
}
