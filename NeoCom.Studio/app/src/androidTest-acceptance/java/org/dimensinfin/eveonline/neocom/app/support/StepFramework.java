package org.dimensinfin.eveonline.neocom.app.support;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.dimensinfin.android.mvc.activity.MVCPagerFragment;
import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.eveonline.neocom.app.support.worlds.NeoComWorld;
import org.junit.Assert;

public class StepFramework /*extends AppCredentialSupportTest*/ {
	protected View searchPanelByClass( final Class filterClass, final ViewGroup container ) {
		final int contentCount = container.getChildCount();
		for (int i = 0; i < contentCount; i++) {
			final View target = container.getChildAt(i);
			final IAndroidController controller = (IAndroidController) target.getTag();
			final Object model = controller.getModel();
			if (model.getClass() == filterClass) return target;
		}
		return null;
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

	protected void accessDataContainers( final MVCPagerFragment fragment, final NeoComWorld world ) {
		final ViewGroup headerContainer = fragment.accessHeaderContainer();
		Assert.assertNotNull("Header container ViewGroup exists.", headerContainer);
		world.setHeaderContainer(headerContainer);
		final ListView dataSectionContainer = fragment.accessDataSectionContainer();
		Assert.assertNotNull("Container ListView exists.", dataSectionContainer);
		world.setDataSectionContainer(dataSectionContainer);
	}

}
