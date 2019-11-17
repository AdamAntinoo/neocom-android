package org.dimensinfin.eveonline.neocom.test.support;

import java.util.Objects;

import org.dimensinfin.android.mvc.activity.IPagerFragment;
import org.dimensinfin.android.mvc.activity.MVCMultiPageActivity;

public class ActivityResolver<A extends MVCMultiPageActivity, F extends IPagerFragment> {
	private A activity;

	public ActivityResolver( final A activity ) {
		Objects.requireNonNull(activity);
		this.activity = activity;
	}

	public int pagesCreated( A activity ) {
		return activity.accessPageAdapter().getCount();
	}

	public F fragmentResolver( A activity, int pageNumber ) {
		return (F) activity.accessPageAdapter().getItem( pageNumber );
	}

//	public void getDataContainer (final int pageNumber){
//		Objects.requireNonNull(this.activity);
//		final F fragment = (F) this.activity.accessPageAdapter().getItem( pageNumber );
//		if ( null != fragment){
//			final ListView container = ((MVCPagerFragment) fragment).dataSource
//			.getCount();
//		}
//	}
}
