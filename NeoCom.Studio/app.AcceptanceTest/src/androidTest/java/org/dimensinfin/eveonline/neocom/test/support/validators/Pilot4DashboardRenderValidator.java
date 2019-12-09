package org.dimensinfin.eveonline.neocom.test.support.validators;

import android.widget.TextView;

import org.junit.Assert;

import org.dimensinfin.eveonline.neocom.R;

public class Pilot4DashboardRenderValidator extends PanelValidator {
	private static final String PILOT_NAME = "PilotName";
	private static final String PILOT_IDENTIFIER = "PilotId";
	private static final String PILOT_RACE = "PilotRace";
	private static final String CORPORATION_NAME = "CorporationName";

	private Pilot4DashboardRenderValidator() {}

	@Override
	public boolean validate() {
		Assert.assertEquals( this.rowData.get( PILOT_NAME ),
				((TextView) this.targetView.findViewById( R.id.pilotName )).getText() );
		Assert.assertEquals( this.rowData.get( PILOT_IDENTIFIER ),
				((TextView) this.targetView.findViewById( R.id.pilotIdentifier )).getText() );
		Assert.assertEquals( this.rowData.get( PILOT_RACE ),
				((TextView) this.targetView.findViewById( R.id.raceName )).getText() );
		Assert.assertEquals( this.rowData.get( CORPORATION_NAME ),
				((TextView) this.targetView.findViewById( R.id.corporationName )).getText() );
		return true;
	}

	// - B U I L D E R
	public static class Builder extends PanelValidator.Builder<Pilot4DashboardRenderValidator, Pilot4DashboardRenderValidator.Builder> {
		private Pilot4DashboardRenderValidator onConstruction;

		@Override
		protected Pilot4DashboardRenderValidator getActual() {
			if (null == this.onConstruction) this.onConstruction = new Pilot4DashboardRenderValidator();
			return this.onConstruction;
		}

		@Override
		protected Pilot4DashboardRenderValidator.Builder getActualBuilder() {
			return this;
		}
	}
}
