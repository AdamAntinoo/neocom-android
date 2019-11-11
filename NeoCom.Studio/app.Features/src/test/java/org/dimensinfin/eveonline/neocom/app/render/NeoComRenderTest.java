package org.dimensinfin.eveonline.neocom.app.render;

import android.content.Context;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.render.core.NeoComRender;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class NeoComRenderTest {
	@Test
	public void format2hms() {
		final IAndroidController controller = Mockito.mock(IAndroidController.class);
		final Context context = Mockito.mock(Context.class);
		final NeoComRender render = new NeoComRender(controller, context) {
			@Override
			public int accessLayoutReference() {
				return R.layout.extractor4colony;
			}
		};

		final String minute = render.format2hms(60);
		Assert.assertEquals("Validate one minute.", "00:01:00", minute);
		final String hour = render.format2hms(60 + 3600);
		Assert.assertEquals("Validate one hour and one minute.", "01:01:00", hour);
		final String cycle = render.format2hms(7200);
		Assert.assertEquals("Validate one cycle.", "02:00:00", cycle);
	}
}
