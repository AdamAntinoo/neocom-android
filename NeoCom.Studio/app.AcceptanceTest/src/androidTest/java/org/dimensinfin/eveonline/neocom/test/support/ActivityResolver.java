package org.dimensinfin.eveonline.neocom.test.support;

import org.dimensinfin.android.mvc.activity.IPagerFragment;
import org.dimensinfin.android.mvc.activity.MVCMultiPageActivity;

public class ActivityResolver<A extends MVCMultiPageActivity, F extends IPagerFragment> {
	public int pagesCreated( A activity ) {
		return activity.accessPageAdapter().getCount();
	}

	public F fragmentResolver( A activity, int pageNumber ) {
		return (F) activity.accessPageAdapter().getItem( pageNumber );
	}
}