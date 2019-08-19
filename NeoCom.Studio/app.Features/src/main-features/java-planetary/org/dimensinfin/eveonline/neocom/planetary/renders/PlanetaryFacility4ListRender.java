package org.dimensinfin.eveonline.neocom.planetary.renders;

import android.content.Context;
import android.widget.TextView;
import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.planetary.controller.PlanetaryFacilityController;

import org.joda.time.DateTime;

public class PlanetaryFacility4ListRender extends ResourceFacetedRender {
	private TextView facilityName;
	private TextView cycleStart;
	private TextView cycleElapsed;

	public PlanetaryFacility4ListRender( @NonNull final IAndroidController controller, @NonNull final Context context ) {
		super(controller, context);
	}

	@Override
	public PlanetaryFacilityController getController() {
		return (PlanetaryFacilityController) super.getController();
	}

	// - I R E N D E R
	@Override
	public int accessLayoutReference() {
		return R.layout.planetaryfacility4list;
	}

	@Override
	public void initializeViews() {
		super.initializeViews();
		this.facilityName = this.getView().findViewById(R.id.facilityName);
		this.cycleStart = this.getView().findViewById(R.id.cycleStart);
		this.cycleElapsed = this.getView().findViewById(R.id.cycleElapsed);
	}

	@Override
	public void updateContent() {
		super.updateContent();
		this.facilityName.setText(this.getController().getModel().getName());
//		this.cycleStart.setText(this.getController().getModel().getLastCycleStart().toString());
//		this.cycleElapsed.setText(this.accessElapsedCycle());
	}
	protected String accessElapsedCycle(){
		return Long.valueOf(( DateTime.now().getMillis()-this.getController().getModel().getLastCycleStart().getMillis())/1000/60).toString()
				+" minutes";
	}
}
