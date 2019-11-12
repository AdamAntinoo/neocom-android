package org.dimensinfin.eveonline.neocom.test.support;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.test.rule.ActivityTestRule;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.android.mvc.domain.IRender;
import org.dimensinfin.eveonline.neocom.app.activity.AppSplashActivity;

public class StepFramework {
	protected static Logger logger = LoggerFactory.getLogger(StepFramework.class);
	protected NeoComWorld world;

	public StepFramework( final NeoComWorld world ) {
		this.world = world;
	}

	protected void createActivity( final String activityName ) {
		switch (activityName) {
			case "AppSplashActivity":
				final ActivityTestRule<AppSplashActivity> activityRuleSplash = new ActivityTestRule<>(AppSplashActivity.class,
				                                                                                      false, false);
				Assert.assertNotNull(activityRuleSplash);
				this.world.setActivityRule(activityRuleSplash);
				break;
//			case "MiningLedgerActivity":
//				final ActivityTestRule<MiningLedgerActivity> activityRuleMiningLegder =
//						new ActivityTestRule<>(MiningLedgerActivity.class, false, false);
//				Assert.assertNotNull(activityRuleMiningLegder);
//				this.world.setActivityRule(activityRuleMiningLegder);
//				break;
		}
	}

	//	protected void launchActivity() {
//		this.world.getActivityRule().launchActivity(null);
//		this.world.setActiveActivity((FragmentActivity) this.world.getActivityRule().getActivity());
//		Assert.assertNotNull(this.world.getActiveActivity());
//	}
	protected View searchFirstPanelByClass( final Class filterClass, final ViewGroup container ) {
		final int contentCount = container.getChildCount();
		for (int i = 0; i < contentCount; i++) {
			final View target = container.getChildAt(i);
			final IAndroidController controller = (IAndroidController) target.getTag();
			final Object model = controller.getModel();
			if (model.getClass() == filterClass) return target;
		}
		return null;
	}

	protected View searchFirstPanelByRenderClass( final String filterClassName, final ListView container ) {
		final int contentCount = container.getChildCount();
		for (int i = 0; i < contentCount; i++) {
			final View target = container.getChildAt(i);
			final IAndroidController controller = (IAndroidController) target.getTag();
			final IRender render = controller.buildRender(
					this.world.getActiveActivity().getBaseContext());
			if (render.getClass().getSimpleName().equals(filterClassName)) return target;
		}
		return null;
	}

	// ----

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


	protected List<View> accessPanelsOfClass( final Class filterClass, final ListView container ) {
		final List<View> panels = new ArrayList<>();
		final int contentCount = container.getChildCount();
		for (int i = 0; i < contentCount; i++) {
			final View target = this.getViewByPosition(i, container);
			final IAndroidController controller = (IAndroidController) target.getTag();
			final Object model = controller.getModel();
			if (model.getClass() == filterClass) panels.add(target);
		}
		return panels;
	}

	protected List<View> accessPanelsOfClass( final Class filterClass, final ViewGroup container ) {
		final List<View> panels = new ArrayList<>();
		final int contentCount = container.getChildCount();
		for (int i = 0; i < contentCount; i++) {
			final View target = container.getChildAt(i);
			final IAndroidController controller = (IAndroidController) target.getTag();
			final Object model = controller.getModel();
			if (model.getClass() == filterClass) panels.add(target);
		}
		return panels;
	}
}
