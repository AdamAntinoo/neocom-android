package org.dimensinfin.eveonline.neocom.test.support;

import android.app.ActionBar;
import android.view.View;
import androidx.test.platform.app.InstrumentationRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.dimensinfin.android.mvc.activity.MVCPagerFragment;
import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.eveonline.neocom.app.activity.core.NeoComActivity;
import org.dimensinfin.eveonline.neocom.assets.controller.RegionController;
import org.dimensinfin.eveonline.neocom.assets.controller.StationController;
import org.dimensinfin.eveonline.neocom.domain.space.Region;

public class ActivityResolverNew {
	private NeoComActivity activity;
	private int selectedPage = 0;
//	private MVCPagerFragment fragment;

	public ActivityResolverNew( final NeoComActivity activity ) {
		Objects.requireNonNull( activity );
		this.activity = activity;
		this.move2Page( this.selectedPage );
	}

	public MVCPagerFragment getFragment() {
		return (MVCPagerFragment) this.activity.accessPageAdapter().getItem( this.selectedPage );
	}

	public int pageCount() {
		return this.activity.accessPageAdapter().getCount();
	}

	public boolean move2Page( final int pageNumber ) {
		if (pageNumber >= this.pageCount()) return false;
		else {
			this.selectedPage = pageNumber;
			return true;
		}
	}

	public ActionBar accessActionBar() {
		return this.activity.getActionBar();
	}

	public int headerContentsCount() {
		return this.getFragment().accessHeaderContents().size();
	}

	public int dataContentsCount() {
		return this.getFragment().accessDataContents().size();
	}

	public List<View> matchDataPanel( final String panelType ) {
		final List<View> results = new ArrayList<>();
		switch (panelType) {
			case "Region":
				for (IAndroidController controller : this.getFragment().accessDataContents()) {
					if (controller.getModel() instanceof Region) results.add( controller.getViewCache() );
				}
			case "Station":
				for (IAndroidController controller : this.getFragment().accessDataContents()) {
					if (controller.getModel() instanceof Region) results.add( controller.getViewCache() );
				}
		}
		return results;
	}

	public List<IAndroidController> matchDataController( final String panelType ) {
		final List<IAndroidController> results = new ArrayList<>();
		switch (panelType) {
			case "Region":
				for (IAndroidController controller : this.getFragment().accessDataContents())
					if (controller instanceof RegionController) results.add( controller );
			case "Station":
				for (IAndroidController controller : this.getFragment().accessDataContents())
					if (controller instanceof StationController) results.add( controller );
		}
		return results;
	}

	public void updateDataSectionRender() {
		InstrumentationRegistry.getInstrumentation().waitForIdle( () -> this.getFragment().updateDisplay() );
	}
}
