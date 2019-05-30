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
package org.dimensinfin.eveonline.neocom.app.activity.core;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.sharedcode.dataservice.AndroidGlobalDataManager;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class ALinearActivity extends APublicMenuActivity {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................
//	protected Bundle _extras = null;
	protected ViewGroup _fragmentContainer = null;
	protected FragmentDataContainer _linearAdapter = null;
	/** Image reference to the background layout item that can be replaced by the application implementation. */
	protected ImageView background = null;

	// - M E T H O D - S E C T I O N ..........................................................................
	@Override
	public void onCreate( @Nullable final Bundle savedInstanceState ) {
		logger.info(">> [ALinearActivity.onCreate]");
		super.onCreate(savedInstanceState);
//		// Process the extras received by the intent so they can be shared to all the Fragments
//		try {
//			// Get the parameters and save them on local fields to be stored on destruction and passed to Fragments.
//			if (null != savedInstanceState) {
//				_extras = savedInstanceState;
//			} else {
//				_extras = this.getIntent().getExtras();
//			}
//		} catch (RuntimeException rtex) {
//			logger.warn("RTEX [ALinearActivity.onCreate]> " + rtex.getMessage());
//		}

		// Set the layout to the core activity that defines the background and the fragment container, this time one on top of another and
		// not in different pages.
		try {
			this.setContentView(R.layout.activity_linear);

			// Locate the elements of the page and store in global data.
			_fragmentContainer = (ViewGroup) this.findViewById(R.id.fragmentContainer);
			background = (ImageView) this.findViewById(R.id.backgroundFrame);
			//		_indicator = (CirclePageIndicator) this.findViewById(org.dimensinfin.android.mvc.R.id.indicator);
			// Check page planetaryFacility.
			if (null == _fragmentContainer) {
				throw new RuntimeException("RTEX [ALinearActivity.onCreate]> Expected UI element not found.");
			}
			if (null == background) {
				throw new RuntimeException("RTEX [ALinearActivity.onCreate]> Expected UI element not found.");
			}

			// Add the adapter for the page switching.
			_linearAdapter = new FragmentDataContainer(_fragmentContainer.getId());
		} catch (RuntimeException rtex) {
			AndroidGlobalDataManager.registerRuntimeException("ALinearActivity.onCreate"
					, rtex, AndroidGlobalDataManager.EExceptionSeverity.UNEXPECTED);
			rtex.printStackTrace();
		}
		logger.info("<< [ALinearActivity.onCreate]");
	}

	// - CLASS IMPLEMENTATION ...................................................................................
	public static class FragmentDataContainer {
		// - F I E L D - S E C T I O N ............................................................................
		private int _pagerid = R.id.fragmentContainer;
		protected List<Fragment> _fragments = new ArrayList();
		//		private Iterable<? extends Fragment> fragments;

		// - C O N S T R U C T O R - S E C T I O N ................................................................
		public FragmentDataContainer( final int layoutId ) {
			this._pagerid = layoutId;
		}

		// - M E T H O D - S E C T I O N ..........................................................................

		/**
		 * Return the fragment internal identifier generated by the FragmentPager. This is a composed string from
		 * some fields that have to be informed to this adapter.
		 * @param position the page position identifier we are going to use for this Fragment. This has not to match the real
		 *                 position of the instance on the Fragment internal list, it is only a numeric unique identifier.
		 * @return internal Fragment Manager fragment identifier string.
		 */
		public String getFragmentId( final int position ) {
			return "android:switcher:" + _pagerid + ":" + position;
		}

		public int getNextPosition() {
			return _fragments.size();
		}

		/**
		 * Add a new fragment at the end of the list of already managed fragments.
		 * @param fragNew new fragment to all to the list of pages.
		 */
		public void addLinear( final Fragment fragNew ) {
			if (null != fragNew) {
				_fragments.add(fragNew);
			}
		}

		public List<Fragment> getFragments() {
			return _fragments;
		}
	}
}
// - UNUSED CODE ............................................................................................
//[01]
