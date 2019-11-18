package org.dimensinfin.eveonline.neocom.test.support;

import android.app.ActionBar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.dimensinfin.android.mvc.activity.MVCPagerFragment;
import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.eveonline.neocom.app.activity.core.NeoComActivity;
import org.dimensinfin.eveonline.neocom.domain.space.Region;

public class ActivityResolverNew {
	private NeoComActivity activity;
	private MVCPagerFragment fragment;

	public ActivityResolverNew( final NeoComActivity activity ) {
		Objects.requireNonNull( activity );
		this.activity = activity;
		this.move2Page( 0 );
	}

	public int pageCount() {
		return this.activity.accessPageAdapter().getCount();
	}

	public boolean move2Page( final int pageNumber ) {
		if (pageNumber > this.pageCount()) return false;
		else {
			this.fragment = (MVCPagerFragment) this.activity.accessPageAdapter().getItem( pageNumber );
			return true;
		}
	}

	public ActionBar accessActionBar() {
		return this.activity.getActionBar();
	}

	public int headerContentsCount() {
		return this.fragment.accessHeaderContents().size();
	}

	public int dataContentsCount() {
		return this.fragment.accessDataContents().size();
	}

	public List<View> matchDataPanel( final String panelType ) {
		final List<View> results = new ArrayList<>();
		switch (panelType) {
			case "Region":
				for (IAndroidController controller : this.fragment.accessDataContents()) {
					if (controller.getModel() instanceof Region) results.add( controller.getViewCache() );
				}
		}
		return results;
	}

	public List<IAndroidController> matchDataController( final String panelType ) {
		final List<IAndroidController> results = new ArrayList<>();
		switch (panelType) {
			case "Region":
				for (IAndroidController controller : this.fragment.accessDataContents()) {
					if (controller.getModel() instanceof Region) results.add( controller );
				}
		}
		return results;
	}
//	public F fragmentResolver( A activity, int pageNumber ) {
//		return (F) activity.accessPageAdapter().getItem( pageNumber );
//	}

//	public void getDataContainer (final int pageNumber){
//		Objects.requireNonNull(this.activity);
//		final F fragment = (F) this.activity.accessPageAdapter().getItem( pageNumber );
//		if ( null != fragment){
//			final ListView container = ((MVCPagerFragment) fragment).dataSource
//			.getCount();
//		}
//	}
}
