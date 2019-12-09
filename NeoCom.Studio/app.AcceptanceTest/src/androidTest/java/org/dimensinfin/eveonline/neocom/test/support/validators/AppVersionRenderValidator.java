package org.dimensinfin.eveonline.neocom.test.support.validators;

import android.widget.TextView;

import org.junit.Assert;

import org.dimensinfin.eveonline.neocom.R;

public class AppVersionRenderValidator extends PanelValidator {
	private static final String APP_NAME = "AppName";
	private static final String APP_VERSION = "AppVersion";

	private AppVersionRenderValidator() {}

	@Override
	public boolean validate() {
		Assert.assertEquals( this.rowData.get( APP_NAME ),
				((TextView) this.targetView.findViewById( R.id.applicationName )).getText() );
		Assert.assertEquals( this.rowData.get( APP_VERSION ),
				((TextView) this.targetView.findViewById( R.id.applicationVersion )).getText() );
		return true;
	}

	// - B U I L D E R
	public static class Builder extends PanelValidator.Builder<AppVersionRenderValidator, AppVersionRenderValidator.Builder> {
		private AppVersionRenderValidator onConstruction;

		@Override
		protected AppVersionRenderValidator getActual() {
			if (null == this.onConstruction) this.onConstruction = new AppVersionRenderValidator();
			return this.onConstruction;
		}

		@Override
		protected AppVersionRenderValidator.Builder getActualBuilder() {
			return this;
		}
	}
}
